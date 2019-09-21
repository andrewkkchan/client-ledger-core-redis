package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.redis.service.JournalService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;

public class TopUpProcessorTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProducerImpl resultProcessor;
    @Mock
    private JournalService<TopUpRequest> topUpService;
    @InjectMocks
    private TopUpProcessor topUpProcessor;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws IOException {
        TopUpRequest topUpRequest = new TopUpRequest();
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TopUpRequest.class))).thenReturn(topUpRequest);
        List<JournalEntry> journalEntries = new ArrayList<>();
        Mockito.when(topUpService.journal(nullable(String.class), nullable(TopUpRequest.class), nullable(Integer.class), nullable(long.class)))
                .thenReturn(journalEntries);
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.TOP_UP.toString());
        topUpProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor).produceSuccess(nullable(String.class), nullable(Object.class), nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor, Mockito.never()).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class), nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testProcess_cannotRead() throws IOException {
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TopUpRequest.class))).thenThrow(new IOException());
        List<JournalEntry> journalEntries = new ArrayList<>();
        Mockito.when(topUpService.journal(nullable(String.class), nullable(TopUpRequest.class), nullable(Integer.class), nullable(long.class)))
                .thenReturn(journalEntries);
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.TOP_UP.toString());
        topUpProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class), nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class), nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testProcess_serviceFail() throws IOException {
        TopUpRequest topUpRequest = new TopUpRequest();
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(TopUpRequest.class))).thenReturn(topUpRequest);
        List<JournalEntry> journalEntries = new ArrayList<>();
        Mockito.when(topUpService.journal(nullable(String.class), nullable(TopUpRequest.class), nullable(Integer.class), nullable(long.class)))
                .thenThrow(new InvalidBusinessRuleException("service fails"));
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.TOP_UP.toString());
        topUpProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class), nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class), nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testGetType(){
        Assert.assertEquals(Type.TOP_UP.toString(), topUpProcessor.getType());
    }
}
