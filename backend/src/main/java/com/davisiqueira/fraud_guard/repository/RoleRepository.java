package com.davisiqueira.fraud_guard.repository;

import com.davisiqueira.fraud_guard.model.RoleModel;
import com.davisiqueira.fraud_guard.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(RoleName name);
}
