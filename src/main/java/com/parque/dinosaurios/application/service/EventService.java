package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.model.ParkEvent;
import com.parque.dinosaurios.domain.model.ParkState;
import com.parque.dinosaurios.infrastructure.persistence.entity.ParkEventEntity;
import com.parque.dinosaurios.infrastructure.persistence.repository.ParkEventRepository;
import com.parque.dinosaurios.simulation.RandomProvider;
import com.parque.dinosaurios.simulation.event.ParkEventStrategy;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final ParkProperties properties;
    private final ParkEventRepository repository;
    private final ExpenseService expenseService;
    private final RandomProvider randomProvider;
    private final Map<EventType, ParkEventStrategy> strategies;

    public EventService(
            ParkProperties properties,
            ParkEventRepository repository,
            ExpenseService expenseService,
            RandomProvider randomProvider,
            List<ParkEventStrategy> strategies) {
        this.properties = properties;
        this.repository = repository;
        this.expenseService = expenseService;
        this.randomProvider = randomProvider;
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ParkEventStrategy::type, Function.identity()));
    }

    @Transactional
    public Optional<ParkEvent> maybeGenerate(ParkState state) {
        if (strategies.isEmpty() || !randomProvider.chance(properties.getEvents().getBaseProbability())) {
            return Optional.empty();
        }
        ParkEventStrategy strategy = randomProvider.pick(List.copyOf(strategies.values()));
        return Optional.of(execute(strategy, state));
    }

    @Transactional
    public ParkEvent trigger(EventType type, ParkState state) {
        ParkEventStrategy strategy = Optional.ofNullable(strategies.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Evento no soportado: " + type));
        return execute(strategy, state);
    }

    @Transactional(readOnly = true)
    public List<ParkEvent> findAll() {
        return repository.findAll().stream().map(ParkEventEntity::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<ParkEvent> findActive() {
        return repository.findByStatus(EventStatus.ACTIVE).stream().map(ParkEventEntity::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public Map<EventType, Long> countByType() {
        Map<EventType, Long> totals = new EnumMap<>(EventType.class);
        for (EventType type : EventType.values()) {
            totals.put(type, 0L);
        }
        findAll().forEach(event -> totals.merge(event.type(), 1L, Long::sum));
        return totals;
    }

    private ParkEvent execute(ParkEventStrategy strategy, ParkState state) {
        ParkEvent event = strategy.execute(state);
        ParkEvent saved = repository.save(ParkEventEntity.from(event)).toDomain();
        state.registerEvent(saved);
        recordResponseExpense(saved);
        return saved;
    }

    private void recordResponseExpense(ParkEvent event) {
        if (event.type() == EventType.OFFER_HOUR) {
            return;
        }
        ExpenseType expenseType = event.type() == EventType.VEHICLE_FAILURE
                ? ExpenseType.REPAIR
                : ExpenseType.EVENT_RESPONSE;
        BigDecimal amount = BigDecimal.valueOf(event.severity()).multiply(new BigDecimal("75.00"));
        expenseService.record(
                expenseType,
                amount,
                "Respuesta operativa para " + event.type(),
                event.simulationStep());
    }
}
