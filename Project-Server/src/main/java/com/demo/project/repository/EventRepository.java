package com.demo.project.repository;

import com.demo.project.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByLocation_Id(Long id);
    List<EventEntity> findAllByLocation_City(String city);
    List<EventEntity> findAllByOrganizer_Id(Long id);
}
