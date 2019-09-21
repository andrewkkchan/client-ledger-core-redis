package com.industrieit.ledger.clientledger.core.redis.model.ledger.impl;


import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Itemizable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class P2PItemizable implements Itemizable {
    private final Account source;
    private final Account destination;
    private final Account feeAccount;
    private final Account taxAccount;
    private final BigDecimal amount;
    private final BigDecimal fee;
    private final BigDecimal tax;
    private final String currency;
    private final String requestId;

    public P2PItemizable(Account source, Account destination, Account feeAccount, Account taxAccount,
                         BigDecimal amount, BigDecimal fee, BigDecimal tax, String currency, String requestId) {
        this.source = source;
        this.destination = destination;
        this.feeAccount = feeAccount;
        this.taxAccount = taxAccount;
        this.amount = amount;
        this.fee = fee;
        this.tax = tax;
        this.currency = currency;
        this.requestId = requestId;
    }

    @Override
    public List<JournalEntry> itemize() {
        List<JournalEntry> journalEntries = new ArrayList<>();
        journalEntries.add(new JournalEntry(source.getId(), currency, amount.negate(), getRequestId()));
        journalEntries.add(new JournalEntry(destination.getId(), currency, amount,getRequestId()));
        journalEntries.add(new JournalEntry(source.getId(), currency, fee.negate(), getRequestId()));
        journalEntries.add(new JournalEntry(feeAccount.getId(), currency, fee, getRequestId()));
        journalEntries.add(new JournalEntry(source.getId(), currency, tax.negate(), getRequestId()));
        journalEntries.add(new JournalEntry(taxAccount.getId(), currency, tax, getRequestId()));
        return journalEntries;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }
}
