package com.industrieit.ledger.clientledger.core.redis.model.request.impl;

import com.industrieit.ledger.clientledger.core.redis.model.request.EventRequest;

import java.math.BigDecimal;

public class TopUpRequest implements EventRequest {
    private String currency;
    private String topUpAccount;
    private String settlementAccount;
    private BigDecimal amount;

    public String getCurrency() {
        return currency;
    }

    public String getTopUpAccount() {
        return topUpAccount;
    }

    public String getSettlementAccount() {
        return settlementAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TopUpRequest() {
    }

    public TopUpRequest(String currency, String topUpAccount, String settlementAccount, BigDecimal amount) {
        this.currency = currency;
        this.topUpAccount = topUpAccount;
        this.settlementAccount = settlementAccount;
        this.amount = amount;
    }
}
