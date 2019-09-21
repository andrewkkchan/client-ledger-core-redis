package com.industrieit.ledger.clientledger.core.redis.entity;

import org.junit.Assert;
import org.junit.Test;

public class TransactionEventTest {
    @Test
    public void testGetSet() {
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setType("P2P");
        transactionEvent.setId("12345");
        transactionEvent.setRequest("{}");
        transactionEvent.setCreateTime(10000L);
        Assert.assertEquals("P2P", transactionEvent.getType());
        Assert.assertEquals("12345", transactionEvent.getId());
        Assert.assertEquals("{}", transactionEvent.getRequest());
        Assert.assertEquals(10000, transactionEvent.getCreateTime());

    }
}
