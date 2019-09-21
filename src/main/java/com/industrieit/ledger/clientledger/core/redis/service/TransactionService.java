package com.industrieit.ledger.clientledger.core.redis.service;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;

public interface TransactionService {
    TransactionResult getLastResult();

}
