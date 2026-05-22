package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.domain.enums.ExpenseType;
import com.parque.dinosaurios.domain.model.OperationalExpense;
import com.parque.dinosaurios.infrastructure.persistence.entity.OperationalExpenseEntity;
import com.parque.dinosaurios.infrastructure.persistence.repository.OperationalExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService {

    private final OperationalExpenseRepository repository;

    public ExpenseService(OperationalExpenseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OperationalExpense record(ExpenseType type, BigDecimal amount, String description, int simulationStep) {
        validateAmount(amount);
        OperationalExpense expense = new OperationalExpense(
                null,
                type,
                amount,
                description,
                simulationStep,
                LocalDateTime.now());
        return repository.save(OperationalExpenseEntity.from(expense)).toDomain();
    }

    @Transactional(readOnly = true)
    public List<OperationalExpense> findAll() {
        return repository.findAll().stream().map(OperationalExpenseEntity::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalAmount() {
        return findAll().stream().map(OperationalExpense::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Map<ExpenseType, BigDecimal> totalsByType() {
        Map<ExpenseType, BigDecimal> totals = new EnumMap<>(ExpenseType.class);
        for (ExpenseType type : ExpenseType.values()) {
            totals.put(type, BigDecimal.ZERO);
        }
        findAll().forEach(expense -> totals.merge(expense.type(), expense.amount(), BigDecimal::add));
        return totals;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto del gasto no puede ser negativo");
        }
    }
}
