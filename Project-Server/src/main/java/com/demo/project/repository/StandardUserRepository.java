package com.demo.project.repository;


import com.demo.project.entity.StandardUserEntity;
import com.demo.project.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StandardUserRepository extends JpaRepository<StandardUserEntity, Long> {
    StandardUserEntity findByUserAccountEntity(UserAccountEntity userAccountEntity);

}
