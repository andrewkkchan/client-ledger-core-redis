package com.industrieit.ledger.clientledger.core.redis.model.request.impl;

import com.industrieit.ledger.clientledger.core.redis.model.request.EventRequest;

public class CreateAccountRequest implements EventRequest {
    private String id;
    private String currency;
    private String accountName;
    private String accountGroup;


    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public CreateAccountRequest(String id, String currency, String accountName, String accountGroup) {
        this.id = id;
        this.currency = currency;
        this.accountName = accountName;
        this.accountGroup = accountGroup;
    }

    public CreateAccountRequest() {
    }
}
