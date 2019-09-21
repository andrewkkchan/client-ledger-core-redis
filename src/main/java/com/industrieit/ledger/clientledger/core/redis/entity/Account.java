package com.industrieit.ledger.clientledger.core.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * One of the only TWO building block for ledger, apart from {@link JournalEntry}
 * Represents the places where debit and credit goes
 * All functionality and transaction are provided by adding a new {@link Account}
 * {@link Account} can be grouped into assets, liabilities, profit and loss.
 * Equally, {@link Account} can represent settlement, customer, bank, internal, tax, and fee.
 */
@RedisHash("Account")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account implements Serializable {

    @Id
    private String id;

    @Indexed
    private long createTime = new Date().getTime();

    @Indexed
    private String currency;

    @Indexed
    private String accountName;

    @Indexed
    private String accountGroup;

    @Indexed
    private BigDecimal balance;

    @Indexed
    private Long kafkaOffset = 0L;

    @Indexed
    private Integer kafkaPartition = 0;


    /**
     * @return Account Name which provides useful info for the user and accountant, e.g., "Andrew's customer account"
     */
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return Account Group which provides handy useful grouping for the account, e.g., "Settlement", "Customer", "Fee", Tax"
     * Mainly used for business rule validation
     * But actual grouping of the accounts under different financial statements are out of scope of any ledgers
     * And shall be done by the user application of the ledger (e.g., accounting software, banking module)
     */
    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    /**
     * @return unique identifier of the account
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return creation time of the Account
     */
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return currency code, e.g., "USD", "JPY", "HKD"
     * allow for business rule validation
     * but actually not necessary if validation can be done out of ledger
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(Long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }

    public Integer getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(Integer kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }
}
