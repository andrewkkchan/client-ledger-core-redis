package com.industrieit.ledger.clientledger.core.redis.controller;

import com.google.common.collect.Iterables;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.repository.JournalEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.nullable;

public class JournalEntryControllerTest {
    @Mock
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryController journalEntryController;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll(){
        Mockito.when(journalEntryRepository.findAll()).thenReturn(new ArrayList<>());
        Iterable<JournalEntry> all = journalEntryController.getAll();
        Assert.assertNotNull(all);
        Assert.assertEquals(0, Iterables.size(all));
    }

    @Test
    public void testGet(){
        Mockito.when(journalEntryRepository.findAllByRequestId(nullable(String.class)))
                .thenReturn(new ArrayList<>());
        Iterable<JournalEntry> journalEntries = journalEntryController.get("12345");
        Assert.assertNotNull(journalEntries);
        Assert.assertEquals(0, Iterables.size(journalEntries));
    }
}
