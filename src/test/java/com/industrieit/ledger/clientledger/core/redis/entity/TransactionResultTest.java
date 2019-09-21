package com.industrieit.ledger.clientledger.core.redis.entity;

import org.junit.Assert;
import org.junit.Test;

public class TransactionResultTest {
    @Test
    public void testGetSet(){
        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setSuccess(true);
        transactionResult.setResponse("{}");
        transactionResult.setId("12345");
        transactionResult.setRequestId("444");
        transactionResult.setCreateTime(1999L);
        Assert.assertEquals("{}", transactionResult.getResponse());
        Assert.assertEquals("12345", transactionResult.getId());
        Assert.assertEquals("444", transactionResult.getRequestId());
        Assert.assertEquals(1999L, transactionResult.getCreateTime());
        Assert.assertTrue(transactionResult.isSuccess());

    }
}
