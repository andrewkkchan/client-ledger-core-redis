package com.industrieit.ledger.clientledger.core.redis.controller;

import com.google.common.collect.Lists;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionEventRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.TransactionResultRepository;
import com.industrieit.ledger.clientledger.core.redis.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

/**
 * REST Controller which is exclusively allowed to POST on the Ledger through creating and enqueuing {@link TransactionEvent}
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionEventRepository transactionEventRepository;
    private final TransactionResultRepository transactionResultRepository;
    private final TransactionService transactionService;

    public TransactionController(TransactionEventRepository transactionEventRepository, TransactionResultRepository transactionResultRepository, TransactionService transactionService) {
        this.transactionEventRepository = transactionEventRepository;
        this.transactionResultRepository = transactionResultRepository;
        this.transactionService = transactionService;
    }

    /**
     * GET one {@link TransactionResult} based on request ID
     *
     * @param requestId ID which uniquely identifies the {@link TransactionResult}
     * @return {@link TransactionResult}
     */
    @GetMapping(value = "/result/event/{requestId}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionResult getResult(@PathVariable String requestId) {
        Optional<TransactionResult> byId = transactionResultRepository.findByRequestId(requestId);
        return byId.orElse(null);
    }

    @GetMapping(value = "/result/current",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionResult getLastResult() {
        return transactionService.getLastResult();
    }


    /**
     * GET all {@link TransactionResult} produced by the Ledger
     *
     * @return all {@link TransactionResult}
     */
    @GetMapping(value = "/result",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<TransactionResult> getAllResult() {
        return transactionResultRepository.findAll();
    }

    /**
     * GET one {@link TransactionEvent} based on request ID
     *
     * @param id ID which uniquely identifies the {@link TransactionEvent}
     * @return {@link TransactionEvent}
     */
    @GetMapping(value = "/event/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionEvent getEvent(@PathVariable String id) {
        Optional<TransactionEvent> byId = transactionEventRepository.findById(id);
        return byId.orElse(null);
    }

    @GetMapping(value = "/event/current",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public TransactionEvent getLastEvent() {
        Iterable<TransactionEvent> all = transactionEventRepository.findAll();
        return Collections.max(Lists.newArrayList(all), (o1, o2) -> {
            long diff = o1.getKafkaOffset() - o2.getKafkaOffset();
            if (diff > 0) {
                return 1;
            }
            if (diff == 0) {
                return 0;
            }
            return -1;
        });
    }


    /**
     * GET all {@link TransactionEvent} enqueued for the Ledger
     *
     * @return all {@link TransactionEvent}
     */
    @GetMapping(value = "/event",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<TransactionEvent> getAllEvent() {
        return transactionEventRepository.findAll();
    }


}
