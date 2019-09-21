package com.industrieit.ledger.clientledger.core.redis.consumer;


import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Producer to produce {@link TransactionResult} for each of the {@link TransactionEvent}
 */
public interface Producer {
    /**
     * Produce {@link TransactionEvent} which records a processing error
     * @param requestId unique ID which matches to the {@link TransactionEvent}
     * @param e {@link InvalidBusinessRuleException} which represents a processing error usually on violation of business rules
     * @param kafkaPartition
     * @param kafkaOffset
     */
    void produceError(String requestId, InvalidBusinessRuleException e, Integer kafkaPartition, long kafkaOffset);

    /**
     * Produce {@link TransactionEvent} which records a processing success
     * @param <T> type of the response, can be anything which best represents the processing success (e.g., journals committed, account created, snapshot timestamp)
     * @param requestId unique ID which matches to the {@link TransactionEvent}
     * @param response object which is serialized into JSON and written to the response field of {@link TransactionResult}
     * @param kafkaPartition
     * @param kafkaOffset
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    <T> void produceSuccess(String requestId, T response, Integer kafkaPartition, long kafkaOffset);

}
