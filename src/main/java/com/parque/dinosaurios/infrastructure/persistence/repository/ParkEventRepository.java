package com.parque.dinosaurios.infrastructure.persistence.repository;

import com.parque.dinosaurios.domain.enums.EventStatus;
import com.parque.dinosaurios.infrastructure.persistence.entity.ParkEventEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkEventRepository extends JpaRepository<ParkEventEntity, Long> {

    List<ParkEventEntity> findByStatus(EventStatus status);
}
