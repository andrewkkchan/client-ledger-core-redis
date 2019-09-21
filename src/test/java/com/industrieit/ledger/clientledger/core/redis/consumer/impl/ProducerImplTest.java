package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionResultRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.nullable;

public class ProducerImplTest {
    @Mock
    private TransactionResultRepository transactionResultRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, TransactionResult> kafkaTemplate;

    @InjectMocks
    ProducerImpl transactionResultProducer;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(kafkaTemplate.send(nullable(String.class), nullable(TransactionResult.class))).thenReturn(null);
    }

    @Test
    public void testProcessSuccessfulTransaction() throws JsonProcessingException {
        Mockito.when(transactionResultRepository.save(nullable(TransactionResult.class))).thenReturn(new TransactionResult());
        Mockito.when(objectMapper.writeValueAsString(nullable(Object.class))).thenReturn("{}");
        transactionResultProducer.produceSuccess(null, null, 0, 0L);
        Mockito.verify(transactionResultRepository).save(nullable(TransactionResult.class));
        Mockito.verify(kafkaTemplate).send(nullable(String.class), nullable(TransactionResult.class));
    }

    @Test
    public void testProcessSuccessfulTransaction_cannotRead() throws JsonProcessingException {
        Mockito.when(transactionResultRepository.save(nullable(TransactionResult.class))).thenReturn(new TransactionResult());
        Mockito.when(objectMapper.writeValueAsString(nullable(Object.class))).thenThrow(new JsonEOFException(null, null, null));
        transactionResultProducer.produceSuccess(null, null, 0  , 0L);
        Mockito.verify(transactionResultRepository).save(nullable(TransactionResult.class));
        Mockito.verify(kafkaTemplate).send(nullable(String.class), nullable(TransactionResult.class));
    }

    @Test
    public void testProduceErrorResult() {
        Mockito.when(transactionResultRepository.save(nullable(TransactionResult.class))).thenReturn(new TransactionResult());
        transactionResultProducer.produceError(null, new InvalidBusinessRuleException("test"), 0, 0L);
        Mockito.verify(transactionResultRepository).save(nullable(TransactionResult.class));
        Mockito.verify(kafkaTemplate).send(nullable(String.class), nullable(TransactionResult.class));
    }

    @Test
    public void testProduceErrorResult_null() {
        Mockito.when(transactionResultRepository.save(nullable(TransactionResult.class))).thenReturn(new TransactionResult());
        transactionResultProducer.produceError(null, null, 0, 0L);
        Mockito.verify(transactionResultRepository).save(nullable(TransactionResult.class));
        Mockito.verify(kafkaTemplate).send(nullable(String.class), nullable(TransactionResult.class));
    }
}
