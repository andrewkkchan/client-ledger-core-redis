package com.industrieit.ledger.clientledger.core.redis.model.request.impl;

import com.industrieit.ledger.clientledger.core.redis.model.request.EventRequest;

import java.math.BigDecimal;

public class P2PRequest implements EventRequest {

    private String currency;
    private String fromCustomerAccount;
    private String toCustomerAccount;
    private String feeAccount;
    private String taxAccount;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal tax;


    public BigDecimal getAmount() {
        return amount;
    }

    public String getFromCustomerAccount() {
        return fromCustomerAccount;
    }

    public String getToCustomerAccount() {
        return toCustomerAccount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFeeAccount() {
        return feeAccount;
    }

    public String getTaxAccount() {
        return taxAccount;
    }


    public P2PRequest() {
    }

    public P2PRequest(String currency, String fromCustomerAccount, String toCustomerAccount, String feeAccount, String taxAccount, BigDecimal amount, BigDecimal fee, BigDecimal tax) {
        this.currency = currency;
        this.fromCustomerAccount = fromCustomerAccount;
        this.toCustomerAccount = toCustomerAccount;
        this.feeAccount = feeAccount;
        this.taxAccount = taxAccount;
        this.amount = amount;
        this.fee = fee;
        this.tax = tax;
    }
}
