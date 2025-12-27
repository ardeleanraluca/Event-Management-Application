package com.demo.project.repository;

import com.demo.project.entity.EventEntity;
import com.demo.project.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    void deleteAllByEvent(EventEntity eventEntity);
}
