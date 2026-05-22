package com.parque.dinosaurios.infrastructure.persistence.repository;

import com.parque.dinosaurios.infrastructure.persistence.entity.RevenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueRepository extends JpaRepository<RevenueEntity, Long> {
}
