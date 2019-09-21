package com.industrieit.ledger.clientledger.core.redis.ledger.validator.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

public class TopUpValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private TopUpValidator topUpValidator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidate(){
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(topUpAccount));
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.of(settlementAccount));
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(9999));
        topUpValidator.validate("1234567", topUpRequest);
    }

    @Test
    public void testValidate_noTopUpAccount(){
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Top-up account not found");
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.empty());
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.of(settlementAccount));
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(9999));
        topUpValidator.validate("1234567", topUpRequest);
    }

    @Test
    public void testValidate_noSettlementAccount(){
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Settlement account not found");
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(topUpAccount));
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.empty());
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(9999));
        topUpValidator.validate("1234567", topUpRequest);
    }

    @Test
    public void testValidate_negativeAmount(){
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Negative amount not supported");
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(topUpAccount));
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.of(settlementAccount));
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(-9999));
        topUpValidator.validate("1234567", topUpRequest);
    }

    @Test
    public void testValidate_exchangeTopUp(){
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for top-up account");
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("JPY");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(topUpAccount));
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.of(settlementAccount));
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(9999));
        topUpValidator.validate("1234567", topUpRequest);
    }

    @Test
    public void testValidate_exchangeSettlement(){
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for settlement account");
        Account topUpAccount = new Account();
        topUpAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(topUpAccount));
        Account settlementAccount = new Account();
        settlementAccount.setCurrency("JPY");
        Mockito.when(accountRepository.findById("67890")).thenReturn(Optional.of(settlementAccount));
        TopUpRequest topUpRequest = new TopUpRequest("USD", "12345", "67890", BigDecimal.valueOf(9999));
        topUpValidator.validate("1234567", topUpRequest);
    }
}
