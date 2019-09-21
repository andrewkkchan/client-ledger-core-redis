package com.industrieit.ledger.clientledger.core.redis.entity;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class JournalEntryTest {
    @Test
    public void testGetSet(){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.TEN);
        journalEntry.setAccountId("1234");
        journalEntry.setCreateTime(10000L);
        journalEntry.setCurrency("USD");
        journalEntry.setId("12345");
        journalEntry.setRequestId("1234567");
        Assert.assertEquals(BigDecimal.TEN, journalEntry.getAmount());
        Assert.assertEquals("1234", journalEntry.getAccountId());
        Assert.assertEquals(10000L, journalEntry.getCreateTime());
        Assert.assertEquals("USD", journalEntry.getCurrency());
        Assert.assertEquals("12345", journalEntry.getId());
        Assert.assertEquals("1234567", journalEntry.getRequestId());

    }
}
