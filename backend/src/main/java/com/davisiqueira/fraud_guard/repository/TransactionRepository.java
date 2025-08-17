package com.davisiqueira.fraud_guard.repository;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    List<TransactionModel> findAllByCpf(String cpf);
}
