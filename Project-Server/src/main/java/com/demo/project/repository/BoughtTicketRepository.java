package com.demo.project.repository;

import com.demo.project.entity.BoughtTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoughtTicketRepository extends JpaRepository<BoughtTicketEntity, Long>{
    List<BoughtTicketEntity> getBoughtTicketEntitiesByUser_Id(Long id);

}
