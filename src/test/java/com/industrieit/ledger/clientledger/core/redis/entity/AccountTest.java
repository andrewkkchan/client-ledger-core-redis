package com.industrieit.ledger.clientledger.core.redis.entity;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest {
    @Test
    public void testGetSet(){
        Account account = new Account();
        account.setCurrency("USD");
        account.setAccountName("Hello");
        account.setAccountGroup("Settlement");
        account.setId("1234");
        account.setCreateTime(10000L);
        Assert.assertEquals("USD", account.getCurrency());
        Assert.assertEquals("Hello", account.getAccountName());
        Assert.assertEquals("Settlement", account.getAccountGroup());
        Assert.assertEquals("1234", account.getId());
        Assert.assertEquals(10000L, account.getCreateTime());
    }
}
