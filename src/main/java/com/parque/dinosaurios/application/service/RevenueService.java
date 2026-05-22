package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.model.Revenue;
import com.parque.dinosaurios.infrastructure.persistence.entity.RevenueEntity;
import com.parque.dinosaurios.infrastructure.persistence.repository.RevenueRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RevenueService {

    private final RevenueRepository repository;

    public RevenueService(RevenueRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Revenue record(
            RevenueType type,
            BigDecimal amount,
            String description,
            int simulationStep,
            int touristCount) {
        validateAmount(amount);
        Revenue revenue = new Revenue(
                null,
                type,
                amount,
                description,
                simulationStep,
                touristCount,
                LocalDateTime.now());
        return repository.save(RevenueEntity.from(revenue)).toDomain();
    }

    @Transactional(readOnly = true)
    public List<Revenue> findAll() {
        return repository.findAll().stream().map(RevenueEntity::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalAmount() {
        return findAll().stream().map(Revenue::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Map<RevenueType, BigDecimal> totalsByType() {
        Map<RevenueType, BigDecimal> totals = new EnumMap<>(RevenueType.class);
        for (RevenueType type : RevenueType.values()) {
            totals.put(type, BigDecimal.ZERO);
        }
        findAll().forEach(revenue -> totals.merge(revenue.type(), revenue.amount(), BigDecimal::add));
        return totals;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto del ingreso no puede ser negativo");
        }
    }
}
