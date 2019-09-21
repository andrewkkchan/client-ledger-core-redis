package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.redis.consumer.Producer;
import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.redis.service.JournalService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class TopUpProcessor implements Processor {
    private final ObjectMapper objectMapper;
    private final Producer producer;
    private final JournalService<TopUpRequest> topUpService;

    public TopUpProcessor(ObjectMapper objectMapper,
                          Producer producer, JournalService<TopUpRequest> topUpService) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.topUpService = topUpService;
    }


    @Override
    public void process(TransactionEvent transactionEvent) {
        String requestId = transactionEvent.getId();

        TopUpRequest topUpRequest;
        try {
            topUpRequest = objectMapper.readValue(transactionEvent.getRequest(), TopUpRequest.class);
        } catch (IOException e) {
            producer.produceError(requestId, new InvalidBusinessRuleException("Malformed request"),
                    transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
            return;
        }

        try {
            journalAndProduce(requestId, topUpRequest, transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
        } catch (InvalidBusinessRuleException e) {
            producer.produceError(requestId, e, transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void journalAndProduce(String requestId, TopUpRequest topUpRequest, Integer kafkaPartition, long kafkaOffset) {
        Iterable<JournalEntry> transactionLogs = this.topUpService.journal(requestId, topUpRequest, kafkaPartition, kafkaOffset);
        producer.produceSuccess(requestId, transactionLogs, kafkaPartition, kafkaOffset);
    }

    @Override
    public String getType() {
        return Type.TOP_UP.toString();
    }
}
