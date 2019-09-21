package com.industrieit.ledger.clientledger.core.redis.ledger.validator.impl;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.P2PRequest;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.JournalEntryRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.nullable;

public class P2PValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JournalEntryRepository journalEntryRepository;
    @InjectMocks
    private P2PValidator p2PValidator;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidate() {
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-3));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_notEnoughFund() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Not enough fund to P2P");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        fromAccount.setBalance(BigDecimal.ZERO);
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-7));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_notSupportCurrencyExchangeSource() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for source account");
        Account fromAccount = new Account();
        fromAccount.setCurrency("JPY");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_notSupportCurrencyExchangeDestination() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for destination account");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account toAccount = new Account();
        toAccount.setCurrency("JPY");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_notSupportCurrencyExchangeFee() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for fee account");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("JPY");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_notSupportCurrencyExchangeTax() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Currency exchange not supported for tax account");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("JPY");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_negativeAmount() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Negative amount not supported");
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(-100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_negativeFee() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Negative fee not supported");
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(-10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_negativeTax() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Negative tax not supported");
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(-5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_noSourceAccount() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Source account not found");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-7));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_noDestinationAccount() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Destination account not found");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-7));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_noFeeAccount() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Fee account not found");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-7));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_noTaxAccount() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Tax account not found");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.empty());
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(-7));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }

    @Test
    public void testValidate_noAccountBalance() {
        thrown.expect(InvalidBusinessRuleException.class);
        thrown.expectMessage("Not enough fund to P2P");
        Account fromAccount = new Account();
        fromAccount.setCurrency("USD");
        fromAccount.setBalance(BigDecimal.ZERO);
        Account toAccount = new Account();
        toAccount.setCurrency("USD");
        Account feeAccount = new Account();
        feeAccount.setCurrency("USD");
        Account taxAccount = new Account();
        taxAccount.setCurrency("USD");
        Mockito.when(accountRepository.findById("12345")).thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findById("23456")).thenReturn(Optional.of(toAccount));
        Mockito.when(accountRepository.findById("34567")).thenReturn(Optional.of(feeAccount));
        Mockito.when(accountRepository.findById("45678")).thenReturn(Optional.of(taxAccount));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(150));
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.findAllByAccountId(nullable(String.class)))
                .thenReturn(journalEntries);
        P2PRequest p2PTransaction = new P2PRequest("USD", "12345",
                "23456", "34567", "45678", BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5));
        p2PValidator.validate("1234567", p2PTransaction);
    }
}
