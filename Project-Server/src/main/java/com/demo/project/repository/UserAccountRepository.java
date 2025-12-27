package com.demo.project.repository;

import com.demo.project.entity.RoleEntity;
import com.demo.project.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    Boolean existsByEmail(String email);

    Optional<UserAccountEntity> findByEmail(String email);

    Optional<List<UserAccountEntity>> findAllByRole(RoleEntity role);

    void deleteById(Long id);
}
