package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.redis.consumer.Consumer;
import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionEventRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionResultRepository;
import com.industrieit.ledger.clientledger.core.redis.service.TransactionService;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class ConsumerImpl implements Consumer, Runnable {
    private final List<Processor> processors;
    private final ObjectMapper objectMapper;
    private final TransactionEventRepository transactionEventRepository;
    private final TransactionResultRepository transactionResultRepository;
    private final org.apache.kafka.clients.consumer.Consumer<String, String> kafkaConsumer;
    private final ExecutorService executorService;
    private final Logger logger;
    private final TransactionService transactionService;
    public static final String TOPIC = "Transaction_Event";
    boolean running = true;

    @Autowired
    public ConsumerImpl(List<Processor> processors, ObjectMapper objectMapper,
                        TransactionEventRepository transactionEventRepository,
                        TransactionResultRepository transactionResultRepository,
                        org.apache.kafka.clients.consumer.Consumer<String, String> kafkaConsumer,
                        ExecutorService executorService, Logger logger,
                        TransactionService transactionService) {
        this.processors = processors;
        this.objectMapper = objectMapper;
        this.transactionEventRepository = transactionEventRepository;
        this.transactionResultRepository = transactionResultRepository;
        this.kafkaConsumer = kafkaConsumer;
        this.executorService = executorService;
        this.logger = logger;
        this.transactionService = transactionService;
    }

    @PostConstruct
    public void init() {
        executorService.submit(this);
    }

    @PreDestroy
    public void destroy() {
        kafkaConsumer.unsubscribe();
        executorService.shutdown();
    }

    @Override
    public void run() {
        TransactionResult lastResult = transactionService.getLastResult();
        kafkaConsumer.subscribe(Collections.singleton(TOPIC), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                if (lastResult == null){
                    kafkaConsumer.seekToBeginning(partitions);
                } else {
                    for (TopicPartition topicPartition : partitions) {
                        if (topicPartition.partition() == lastResult.getKafkaPartition()) {
                            kafkaConsumer.seek(topicPartition, lastResult.getKafkaOffset());
                        }
                    }
                }
            }
        });

        while (running) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> consumerRecord : poll) {
                logger.info("start consuming: " + consumerRecord.value());
                consume(consumerRecord);
                logger.info("done consuming : " + consumerRecord.value());
            }
            kafkaConsumer.commitSync();
        }

    }

    @Override
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        TransactionEvent transactionEvent;
        try {
            transactionEvent = objectMapper.readValue(consumerRecord.value(), TransactionEvent.class);
        } catch (IOException e) {
            return;
        }
        if (!transactionEventRepository.existsById(transactionEvent.getId()) || !transactionResultRepository.findByRequestId(transactionEvent.getId()).isPresent()) {
            transactionEvent.setKafkaOffset(consumerRecord.offset());
            transactionEvent.setKafkaPartition(consumerRecord.partition());
            TransactionEvent save = transactionEventRepository.save(transactionEvent);
            processors
                    .stream()
                    .filter(processor ->
                            processor.getType().equals(transactionEvent.getType()))
                    .findFirst()
                    .ifPresent(processor ->
                            processor.process(save));
        }

    }
}
