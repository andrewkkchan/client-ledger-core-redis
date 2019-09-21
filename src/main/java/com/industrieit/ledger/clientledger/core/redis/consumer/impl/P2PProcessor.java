package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;
import com.industrieit.ledger.clientledger.core.redis.consumer.Producer;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.P2PRequest;
import com.industrieit.ledger.clientledger.core.redis.service.JournalService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class P2PProcessor implements Processor {
    private final ObjectMapper objectMapper;
    private final JournalService<P2PRequest> p2PService;
    private final Producer producer;


    public P2PProcessor(ObjectMapper objectMapper,
                        JournalService<P2PRequest> p2PService, Producer producer) {
        this.objectMapper = objectMapper;
        this.p2PService = p2PService;
        this.producer = producer;
    }

    public void process(TransactionEvent transactionEvent) {
        String requestId = transactionEvent.getId();

        P2PRequest p2PRequest;
        try {
            p2PRequest = objectMapper.readValue(transactionEvent.getRequest(), P2PRequest.class);
        } catch (IOException e) {
            producer.produceError(requestId, new InvalidBusinessRuleException("Malformed request"),
                    transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
            return;
        }
        try {
            journalAndProduce(requestId, p2PRequest, transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
        } catch (InvalidBusinessRuleException e) {
            producer.produceError(requestId, e, transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void journalAndProduce(String requestId, P2PRequest p2PRequest, Integer kafkaPartition, long kafkaOffset) {
        Iterable<JournalEntry> transactionLogs = p2PService.journal(requestId, p2PRequest, kafkaPartition, kafkaOffset);
        producer.produceSuccess(requestId, transactionLogs, kafkaPartition, kafkaOffset);
    }

    @Override
    public String getType() {
        return Type.P2P.toString();
    }


}
