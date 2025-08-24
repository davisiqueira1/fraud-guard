package com.davisiqueira.fraud_guard.repository;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    List<TransactionModel> findAllByUserId(Long id);

    @Query(value = "SELECT * FROM transactions t WHERE t.suspect = false ORDER BY RANDOM() LIMIT :limit;", nativeQuery = true)
    List<TransactionModel> getRandomSample(@Param("limit") int limit);

    @Query(value = "SELECT COUNT(*) FROM transactions t WHERE t.date >= :date", nativeQuery = true)
    int countTransactionSince(@Param("date") LocalDateTime date);

    List<TransactionModel> findAllBySuspect(Boolean suspect);

    List<TransactionModel> findAllBySuspectAndUserId(Boolean suspect, Long id);
}
