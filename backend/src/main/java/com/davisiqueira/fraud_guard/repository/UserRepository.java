package com.davisiqueira.fraud_guard.repository;

import com.davisiqueira.fraud_guard.model.UserModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<UserModel> findWithRolesByEmail(String email);
}
