package com.industrieit.ledger.clientledger.core.redis.model.ledger.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class P2PItemizableTest {
    @Test
    public void testItemize() {
        Account fromAccount = new Account();
        Account toAccount = new Account();
        Account feeAccount = new Account();
        Account taxAccount = new Account();
        P2PItemizable p2PItemizable = new P2PItemizable(fromAccount, toAccount, feeAccount, taxAccount,
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5), "USD", "1234567");
        List<JournalEntry> itemize = p2PItemizable.itemize();
        Assert.assertNotNull(itemize);
        Assert.assertEquals(6, itemize.size());
    }
}
