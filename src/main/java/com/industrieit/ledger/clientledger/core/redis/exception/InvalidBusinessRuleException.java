package com.industrieit.ledger.clientledger.core.redis.exception;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;

/**
 * Run Time Exception thrown during processing of one {@link TransactionEvent}
 */
public class InvalidBusinessRuleException extends RuntimeException {
    public InvalidBusinessRuleException(String message) {
        super(message);
    }
}
