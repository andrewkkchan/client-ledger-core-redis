package com.industrieit.ledger.clientledger.core.redis.service.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.CreateAccountRequest;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;

public class AccountServiceImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAccount() {
        Mockito.when(accountRepository.existsById(nullable(String.class))).thenReturn(false);
        accountService.createAccount(new CreateAccountRequest("1234567", "USD", "Andrew", "Customer"));
        Mockito.verify(accountRepository).save(nullable(Account.class));
    }

    @Test
    public void testCreateAccount_accountExist() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Account already existed");
        Mockito.when(accountRepository.existsById(nullable(String.class))).thenReturn(true);
        accountService.createAccount(new CreateAccountRequest("1234567", "USD", "Andrew", "Customer"));
        Mockito.verify(accountRepository, never()).save(nullable(Account.class));
    }

}
