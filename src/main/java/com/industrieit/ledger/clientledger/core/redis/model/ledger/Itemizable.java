package com.industrieit.ledger.clientledger.core.redis.model.ledger;


import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;

import java.util.List;

/**
 * Itemizable are successful result of business rule validation, which can be itemized into a list of {@link JournalEntry}
 */
public interface Itemizable {
    /**
     * itemized into a list of {@link JournalEntry}
     * @return a list of {@link JournalEntry}
     */
    List<JournalEntry> itemize();

    /**
     * allow tracing of originating request event ID
     * @return the ID which identify the request event
     */
    String getRequestId();
}
