package com.industrieit.ledger.clientledger.core.redis.config;


import org.junit.Assert;
import org.junit.Test;

public class LedgerConfigTest {
    @Test
    public void test(){
        LedgerConfig ledgerConfig = new LedgerConfig();
        Assert.assertNotNull(ledgerConfig.executorService());
        Assert.assertNotNull(ledgerConfig.logger());
        Assert.assertNotNull(ledgerConfig.objectMapper());
    }
}
