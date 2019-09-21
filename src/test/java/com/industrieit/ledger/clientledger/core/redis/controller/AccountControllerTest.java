package com.industrieit.ledger.clientledger.core.redis.controller;

import com.google.common.collect.Iterables;
import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.nullable;

public class AccountControllerTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountController accountController;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll(){
        Mockito.when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<Account> all = accountController.getAll();
        Assert.assertNotNull(all);
        Assert.assertEquals(0, Iterables.size(all));
    }

    @Test
    public void testGet(){
        Mockito.when(accountRepository.findById(nullable(String.class))).thenReturn(Optional.of(new Account()));
        Account account = accountController.get("12345");
        Assert.assertNotNull(account);
    }
}
