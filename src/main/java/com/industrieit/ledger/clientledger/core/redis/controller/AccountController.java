package com.industrieit.ledger.clientledger.core.redis.controller;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller which provides only GET endpoints for {@link Account}.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /**
     * Get all {@link Account} from ledger.
     * @return all {@link Account}
     */
    @GetMapping(value = "/",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Iterable<Account> getAll() {
        return accountRepository.findAll();
    }

    /**
     * Get one {@link Account} which matches the ID
     * @param id account ID
     * @return {@link Account} matching the ID
     */
    @GetMapping(value = "/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseBody
    public Account get(@PathVariable String id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElse(null);
    }
}
