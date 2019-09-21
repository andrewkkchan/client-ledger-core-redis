package com.industrieit.ledger.clientledger.core.redis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionEventRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionResultRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.nullable;

public class TransactionControllerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private TransactionEventRepository transactionEventRepository;
    @Mock
    private TransactionResultRepository transactionResultRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    @InjectMocks
    private TransactionController transactionController;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(kafkaTemplate.send(nullable(String.class), nullable(TransactionEvent.class))).thenReturn(null);
    }

    @Test
    public void testGetResultById() {
        Mockito.when(transactionResultRepository.findByRequestId(nullable(String.class)))
                .thenReturn(Optional.of(new TransactionResult()));
        TransactionResult result = transactionController.getResult("1234");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetAllResult() {
        Mockito.when(transactionResultRepository.findAll())
                .thenReturn(new ArrayList<>());
        Iterable<TransactionResult> result = transactionController.getAllResult();
        Assert.assertNotNull(result);
        Assert.assertEquals(0, Iterables.size(result));
    }

    @Test
    public void testGetAllEvent() {
        Mockito.when(transactionEventRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<TransactionEvent> event = transactionController.getAllEvent();
        Assert.assertNotNull(event);
        Assert.assertEquals(0, Iterables.size(event));
    }

    @Test
    public void testGetEvent() {
        TransactionEvent transactionEvent = new TransactionEvent();
        Mockito.when(transactionEventRepository.findById(nullable(String.class)))
                .thenReturn(Optional.of(transactionEvent));
        TransactionEvent event = transactionController.getEvent("12345");
        Assert.assertNotNull(event);
    }
}
