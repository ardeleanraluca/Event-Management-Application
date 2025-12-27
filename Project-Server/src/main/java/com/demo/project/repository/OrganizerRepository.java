package com.demo.project.repository;

import com.demo.project.entity.OrganizerEntity;
import com.demo.project.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, Long> {
    OrganizerEntity findByUserAccountEntity(UserAccountEntity userAccountEntity);
}
