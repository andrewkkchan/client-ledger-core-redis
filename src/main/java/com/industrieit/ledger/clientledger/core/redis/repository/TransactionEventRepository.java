package com.industrieit.ledger.clientledger.core.redis.repository;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEventRepository extends CrudRepository<TransactionEvent, String> {
}
