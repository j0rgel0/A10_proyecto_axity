package com.parque.dinosaurios.infrastructure.persistence.repository;

import com.parque.dinosaurios.infrastructure.persistence.entity.OperationalExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationalExpenseRepository extends JpaRepository<OperationalExpenseEntity, Long> {
}
