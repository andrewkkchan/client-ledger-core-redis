package com.industrieit.ledger.clientledger.core.redis.controller;

import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.repository.JournalEntryRepository;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller which provides only GET endpoints for {@link JournalEntry}.
 */
@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryController(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    /**
     * GET all {@link JournalEntry} committed to the Ledger
     * @return all {@link JournalEntry}
     */
    @GetMapping(value = "/",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    /**
     * GET all {@link JournalEntry} driven by one {@link TransactionEvent}
     * @param requestId request ID unique to {@link TransactionEvent}
     * @return a list of {@link JournalEntry} related
     */
    @GetMapping(value = "/request/{requestId}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<JournalEntry> get(@PathVariable String requestId) {
        return journalEntryRepository.findAllByRequestId(requestId);
    }
}
