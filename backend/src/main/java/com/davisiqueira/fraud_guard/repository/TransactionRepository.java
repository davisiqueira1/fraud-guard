package com.davisiqueira.fraud_guard.repository;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    Page<TransactionModel> findAllByUserId(Long id, Pageable pageable);
    List<TransactionModel> findAllByUserId(Long id);

    @Query(value = "SELECT * FROM transactions t WHERE t.suspect = false AND t.user_id = :userId ORDER BY RANDOM() LIMIT :limit;", nativeQuery = true)
    List<TransactionModel> getRandomSampleFromUser(@Param("limit") int limit, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM transactions t WHERE t.date >= :date AND t.user_id = :userId", nativeQuery = true)
    int countUserTransactionsSince(@Param("date") LocalDateTime date, @Param("userId") Long userId);

    Page<TransactionModel> findAllBySuspectAndUserId(Boolean suspect, Long id, Pageable pageable);

    Long user(UserModel user);
}
