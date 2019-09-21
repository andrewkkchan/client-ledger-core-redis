package com.industrieit.ledger.clientledger.core.redis.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.redis.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.redis.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.CreateAccountRequest;
import com.industrieit.ledger.clientledger.core.redis.service.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.nullable;

public class CreateAccountProcessorTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AccountService accountService;
    @Mock
    private ProducerImpl resultProcessor;
    @InjectMocks
    private CreateAccountProcessor createAccountProcessor;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws IOException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("112233","JPY","test", "test-group");
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(CreateAccountRequest.class))).thenReturn(createAccountRequest);
        Mockito.when(accountService.createAccount(nullable(CreateAccountRequest.class))).thenReturn(new Account());
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.CREATE_ACCOUNT.toString());
        createAccountProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor).produceSuccess(nullable(String.class), nullable(Object.class), nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor, Mockito.never()).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class), nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testProcess_cannotRead() throws IOException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(CreateAccountRequest.class))).thenThrow(new IOException());
        Mockito.when(accountService.createAccount(nullable(CreateAccountRequest.class))).thenReturn(new Account());
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.CREATE_ACCOUNT.toString());
        createAccountProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class),nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class),nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testProcess_serviceFail() throws IOException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("112233","JPY","test", "test-group");
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(CreateAccountRequest.class))).thenReturn(createAccountRequest);
        Mockito.when(accountService.createAccount(nullable(CreateAccountRequest.class))).thenThrow(new InvalidBusinessRuleException("service fails"));
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setType(Type.CREATE_ACCOUNT.toString());
        createAccountProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class), nullable(Integer.class), nullable(long.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class), nullable(Integer.class), nullable(long.class));
    }

    @Test
    public void testGetType(){
        Assert.assertEquals(Type.CREATE_ACCOUNT.toString(), createAccountProcessor.getType());
    }
}
