package com.industrieit.ledger.clientledger.core.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * One of the only TWO building block for ledger, apart from {@link Account}
 * Represents the only allowable actions to be applied onto any {@link Account}
 * Summing up all {@link JournalEntry} for one {@link Account} can derive {@link AccountBalance}
 * Summing up all {@link JournalEntry} for all {@link Account} in the ledger will always amount to ZERO.
 */
@RedisHash("JournalEntry")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JournalEntry implements Serializable {
    @Id
    private String id;
    @Indexed
    private String requestId;

    /**
     * @return request ID which drives the posting of this journal entry, used for debug and tracing
     */
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Indexed
    private String accountId;

    @Indexed
    private String currency;

    /**
     * @return currency code which the journal entry is denominated in, not necessary but just handy info.
     * Because currency is determined by {@link Account}
     * Each {@link Account} shall have exactly one currency.
     * If a customer needs many currency, just create many {@link Account}
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Indexed
    private long createTime = new Date().getTime();

    @Indexed
    private BigDecimal amount;

    /**
     * @return id for database persisting, not particularly useful
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return create Time
     */
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    /**
     * @return Amount of the journal entry. Can be either positive and negative.
     * amount of one atomic block of journal entries shall also sum up to zero.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public JournalEntry(String accountId, String currency, BigDecimal amount, String requestId) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.requestId = requestId;
    }

    public JournalEntry() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
