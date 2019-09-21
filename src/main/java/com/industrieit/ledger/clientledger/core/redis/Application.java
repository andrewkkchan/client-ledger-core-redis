package com.industrieit.ledger.clientledger.core.redis;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Non-blocking, zero-locking client ledger which produces {@link Account}
 * and {@link JournalEntry} compliance with Accounting Standards, to be fed into
 * General Ledger for reporting purpose.
 * Run on Single Thread Processor behind a queued system. Support at least 100 high-level business transaction per second.
 * Support Atomic Business Transactions which can be itemized into any number of {@link JournalEntry},
 * but all the {@link JournalEntry} must sum up to zero.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    @Value(value = "${auth.domain}")
    String authDomain;

    @Override
    public void run(String... arg0) {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }

    /**
     * Exit Exception on command line args of exitcode
     */
    public static class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
