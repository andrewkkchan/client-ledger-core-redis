package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionEventRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionResultRepository;
import com.industrieit.ledger.clientledger.core.redis.service.TransactionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.nullable;

public class ConsumerImplTest {
    @Mock
    private Processor processor;
    @Mock
    private TransactionEventRepository transactionEventRepository;
    @Mock
    private TransactionResultRepository transactionResultRepository;
    @Mock
    private ExecutorService executorService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Logger logger;
    @Mock
    private org.apache.kafka.clients.consumer.Consumer<String, String> kafkaConsumer;
    @Mock
    private TransactionService transactionService;

    private ConsumerImpl consumer;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        consumer = new ConsumerImpl(Collections.singletonList(processor), objectMapper, transactionEventRepository, transactionResultRepository, kafkaConsumer, executorService, logger, transactionService);
        Mockito.when(processor.getType()).thenReturn("P2P");
    }

    @Test
    public void testConsume_match() throws IOException {
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType("P2P");
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TransactionEvent.class))).thenReturn(transactionEvent);
        ConsumerRecord<String, String> consumerRecord =
                new ConsumerRecord<>(ConsumerImpl.TOPIC, 0, 0, null, new ObjectMapper().writeValueAsString(transactionEvent));
        Mockito.when(transactionEventRepository.existsById(nullable(String.class))).thenReturn(false);
        consumer.consume(consumerRecord);
        Mockito.verify(processor).process(nullable(TransactionEvent.class));
    }

    @Test
    public void testConsume_noMatch() throws IOException {
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType("create-account");
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TransactionEvent.class))).thenReturn(transactionEvent);
        ConsumerRecord<String, String> consumerRecord =
                new ConsumerRecord<>(ConsumerImpl.TOPIC, 0, 0, null, new ObjectMapper().writeValueAsString(transactionEvent));
        Mockito.when(transactionEventRepository.existsById(nullable(String.class))).thenReturn(false);
        consumer.consume(consumerRecord);
        Mockito.verify(processor, Mockito.never()).process(nullable(TransactionEvent.class));
    }

    @Test
    public void testConsume_InvalidBusinessRuleException() throws IOException {
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType("P2P");
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TransactionEvent.class))).thenReturn(transactionEvent);
        Mockito.when(transactionEventRepository.existsById(nullable(String.class))).thenReturn(false);
        ConsumerRecord<String, String> consumerRecord =
                new ConsumerRecord<>(ConsumerImpl.TOPIC, 0, 0, null, new ObjectMapper().writeValueAsString(transactionEvent));
        consumer.consume(consumerRecord);
    }

    @Test
    public void testInit() {
        Mockito.when(executorService.submit(nullable(Runnable.class))).thenReturn(null);
        consumer.init();
    }

    @Test
    public void testDestory() {
        Mockito.when(executorService.shutdownNow()).thenReturn(null);
        consumer.destroy();
    }

    @Test
    public void testRun_notRunning() {
        consumer.running = false;
        consumer.run();
    }

    @Test
    public void testRun_running() throws IOException {
        consumer.running = true;
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType("P2P");
        ConsumerRecord<String, String> consumerRecord =
                new ConsumerRecord<>(ConsumerImpl.TOPIC, 0, 0, null, new ObjectMapper().writeValueAsString(transactionEvent));
        Mockito.when(kafkaConsumer.poll(nullable(Duration.class)))
                .thenReturn(new ConsumerRecords<>(Collections.singletonMap(new TopicPartition("hello", 0), Collections.singletonList(consumerRecord))));
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TransactionEvent.class))).thenReturn(transactionEvent);
        Thread t1 = new Thread(() -> {
            consumer.run();
        });
        t1.start();
        t1.interrupt();
    }

}
