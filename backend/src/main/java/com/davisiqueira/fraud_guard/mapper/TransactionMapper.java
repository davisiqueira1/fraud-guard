package com.davisiqueira.fraud_guard.mapper;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    TransactionResponseDTO toResponseDTO(TransactionModel transaction);
    TransactionModel toModel(TransactionRequestDTO transaction);
}
