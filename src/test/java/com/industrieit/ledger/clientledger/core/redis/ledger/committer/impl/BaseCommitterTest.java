package com.industrieit.ledger.clientledger.core.redis.ledger.committer.impl;

import com.google.common.collect.Iterables;
import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.repository.AccountRepository;
import com.industrieit.ledger.clientledger.core.redis.repository.JournalEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.nullable;

public class BaseCommitterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private JournalEntryRepository journalEntryRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private BaseCommitter baseCommitter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCommit_unBalanced() {
        expectedException.expect(InvalidBusinessRuleException.class);
        expectedException.expectMessage("Unbalanced journal entries");
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntries.add(journalEntry);
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.saveAll(anyIterable())).thenReturn(journalEntries);
        Iterable<JournalEntry> commit = baseCommitter.commit(journalEntries, 0 , 0L);
    }

    @Test
    public void testCommit_balanced() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setId("12345");
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntry.setAccountId("12345");
        journalEntries.add(journalEntry);
        JournalEntry balancingJournalEntry = new JournalEntry();
        balancingJournalEntry.setAmount(BigDecimal.valueOf(-10));
        balancingJournalEntry.setAccountId("23456");
        journalEntries.add(balancingJournalEntry);
        Mockito.when(journalEntryRepository.saveAll(anyIterable())).thenReturn(journalEntries);
        Mockito.when(accountRepository.findById(nullable(String.class))).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(nullable(Account.class))).thenReturn(account);
        Iterable<JournalEntry> commit = baseCommitter.commit(journalEntries, 0, 0L);
        Assert.assertNotNull(commit);
        Assert.assertEquals(2, Iterables.size(commit));
    }

    @Test
    public void testCommit_null() {
        Iterable<JournalEntry> commit = baseCommitter.commit(null, 0, 0L);
        Assert.assertNotNull(commit);
        Assert.assertEquals(0, Iterables.size(commit));
    }
}
