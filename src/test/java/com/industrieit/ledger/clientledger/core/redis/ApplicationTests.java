package com.industrieit.ledger.clientledger.core.redis;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationTests {


    @Test
    public void testRun() {
        Application clientLedgerServiceApplication = new Application();
        clientLedgerServiceApplication.run();
    }

    @Test
    public void testRunWithParam() {
        try {
            Application clientLedgerServiceApplication = new Application();
            clientLedgerServiceApplication.run("exitcode");
        } catch (Application.ExitException e) {
            Assert.assertEquals(10, e.getExitCode());
        }
    }

}
