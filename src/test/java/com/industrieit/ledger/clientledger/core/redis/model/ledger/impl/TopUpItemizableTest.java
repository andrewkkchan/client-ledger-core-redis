package com.industrieit.ledger.clientledger.core.redis.model.ledger.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TopUpItemizableTest {
    @Test
    public void testItemize(){
        Account topUpAccount = new Account();
        Account settlementAccount = new Account();
        TopUpItemizable topUpItemizable = new TopUpItemizable(topUpAccount, settlementAccount, BigDecimal.valueOf(100), "USD", "1234567");
        List<JournalEntry> itemize = topUpItemizable.itemize();
        Assert.assertNotNull(itemize);
        Assert.assertEquals(2, itemize.size());
    }
}
