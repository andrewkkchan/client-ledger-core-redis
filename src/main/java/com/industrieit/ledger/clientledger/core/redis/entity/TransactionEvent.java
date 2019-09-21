package com.industrieit.ledger.clientledger.core.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.industrieit.ledger.clientledger.core.redis.consumer.Consumer;
import com.industrieit.ledger.clientledger.core.redis.consumer.Processor;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

/**
 * Entity which represents an accepted and enqueued high-level transaction, fully packed into a self-contained event
 * {@link TransactionEvent} can be consumed by {@link Consumer}
 * On consumption, exactly one {@link TransactionResult} will be produced and persisted.
 * The full enqueued list of {@link TransactionEvent}, in a strict serial order, will form the basis of Event Sourcing.
 * Event sourcing allows the full state of the ledger be replayed, on any platform and infrastructure, with any processors.
 * This allows in-memory processing and reliable recovery from crash.
 */
@RedisHash("TransactionEvent")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionEvent {

    @Id
    private String id;
    @Indexed
    private String type;
    @Indexed
    private String request;
    @Indexed
    private long createTime = new Date().getTime();
    @Indexed
    private long kafkaOffset;
    @Indexed
    private Integer kafkaPartition;
    /**
     * @return id which uniquely identify this transaction event.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return payload of the request, usually a JSON string
     */
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * @return create time of the event. Important for event sourcing, as the event must be strictly in serial order of this field.
     * Take as the actual creation of enqueuing, which forms the sequence of event sourcing.
     * Can be different from the sending time from the client.
     * Like an event sent at a later time point can happen to arrive at the queue earlier, due to multi-threading.
     * But sequence are in strict order after the point of queueing, forming the basis of event sourcing.
     */
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return type as defined in {@link Type} which calls for correct {@link Processor}
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }

    public Integer getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(Integer kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }
}
