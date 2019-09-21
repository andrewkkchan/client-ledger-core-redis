package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;
import com.industrieit.ledger.clientledger.core.redis.consumer.Producer;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

@Component
public class BackUpProcessor implements Processor {
    private final RedisConnection redisConnection;
    private final Producer producer;


    public BackUpProcessor(RedisConnection redisConnection, Producer producer) {
        this.redisConnection = redisConnection;
        this.producer = producer;
    }

    @Override
    public void process(TransactionEvent transactionEvent) {
        redisConnection.save();
        producer.produceSuccess(transactionEvent.getId(), null,
                transactionEvent.getKafkaPartition(), transactionEvent.getKafkaOffset());

    }

    @Override
    public String getType() {
        return Type.BACK_UP.toString();
    }
}
