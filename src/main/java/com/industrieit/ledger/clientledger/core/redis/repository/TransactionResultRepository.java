package com.industrieit.ledger.clientledger.core.redis.repository;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionResultRepository extends CrudRepository<TransactionResult, String> {
    Optional<TransactionResult> findByRequestId(String requestId);
}
