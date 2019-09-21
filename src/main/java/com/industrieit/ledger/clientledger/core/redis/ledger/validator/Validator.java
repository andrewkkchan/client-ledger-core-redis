package com.industrieit.ledger.clientledger.core.redis.ledger.validator;


import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Itemizable;
import com.industrieit.ledger.clientledger.core.redis.model.request.EventRequest;

/**
 * Validator which validates {@link EventRequest} against all relevant business rules
 * @param <T> Type of the high level transaction event
 */
public interface Validator<T extends EventRequest> {
    /**
     * Validates {@link EventRequest} against all relevant business rules
     * throw runtime error {@link InvalidBusinessRuleException} if any business rule is violated.
     * @param requestId request ID which uniquely identifies one {@link TransactionEvent} which the {@link EventRequest} is based on.
     * @param request {@link EventRequest} to be validated
     * @return {@link Itemizable} if no business rules are violated, and ready for committing as journal entries of the ledger.
     */
    Itemizable validate(String requestId, T request);
}
