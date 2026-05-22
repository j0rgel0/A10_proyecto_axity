package com.parque.dinosaurios.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parque.dinosaurios.application.service.EventService;
import com.parque.dinosaurios.application.service.ExpenseService;
import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.EventType;
import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.infrastructure.persistence.entity.ParkEventEntity;
import com.parque.dinosaurios.infrastructure.persistence.repository.ParkEventRepository;
import com.parque.dinosaurios.simulation.event.OfferHourStrategy;
import com.parque.dinosaurios.simulation.event.VehicleFailureStrategy;
import com.parque.dinosaurios.support.ParkStateFixtures;
import com.parque.dinosaurios.support.StubRandomProvider;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private ParkEventRepository repository;

    @Mock
    private ExpenseService expenseService;

    @Test
    void maybeGeneratePersistsSelectedEventWithoutExpenseForOfferHour() {
        ParkProperties properties = new ParkProperties();
        properties.getEvents().setBaseProbability(1.0);
        when(repository.save(any(ParkEventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        EventService service = new EventService(
                properties,
                repository,
                expenseService,
                new StubRandomProvider().addDouble(0.0).addInt(0),
                List.of(new OfferHourStrategy()));

        var event = service.maybeGenerate(ParkStateFixtures.parkStateWithTourists());

        assertThat(event).isPresent();
        assertThat(event.get().type()).isEqualTo(EventType.OFFER_HOUR);
        verify(repository).save(any(ParkEventEntity.class));
        verify(expenseService, never()).record(any(), any(), any(), anyInt());
    }

    @Test
    void triggerVehicleFailureRecordsRepairExpense() {
        when(repository.save(any(ParkEventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ParkProperties properties = new ParkProperties();
        EventService service = new EventService(
                properties,
                repository,
                expenseService,
                new StubRandomProvider(),
                List.of(new VehicleFailureStrategy()));

        var event = service.trigger(EventType.VEHICLE_FAILURE, ParkStateFixtures.parkStateWithTourists());

        assertThat(event.type()).isEqualTo(EventType.VEHICLE_FAILURE);
        verify(expenseService).record(
                org.mockito.ArgumentMatchers.eq(ExpenseType.REPAIR),
                org.mockito.ArgumentMatchers.any(BigDecimal.class),
                org.mockito.ArgumentMatchers.contains("VEHICLE_FAILURE"),
                org.mockito.ArgumentMatchers.eq(1));
    }
}
