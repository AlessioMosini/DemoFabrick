package com.demo.fabrick;

import com.demo.fabrick.model.Error;
import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.accountTransaction.AccountTransaction;
import com.demo.fabrick.model.accountTransaction.AccountTransactions;
import com.demo.fabrick.service.AccountTransactionService;
import com.demo.fabrick.utility.JsonMapperUtility;
import com.demo.fabrick.utility.ServiceUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountTransactionsMockTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapperUtility jsonMapperUtility;
    @MockBean
    private AccountTransactionService accountTransactionService;

    @Test
    void getAccountTransactions_test_200() throws Exception {
        Response<AccountTransactions> responseMock = new Response<>();
        AccountTransactions accountTransactions = new AccountTransactions();
        AccountTransaction accountTransactionModel = new AccountTransaction();

        accountTransactionModel.setTransactionId("123456");
        accountTransactions.setList(new AccountTransaction[]{accountTransactionModel});

        responseMock.setStatus(Status.OK);
        responseMock.setPayload(accountTransactions);

        ResponseEntity<Response<AccountTransactions>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.OK);

        when(accountTransactionService.getAccountTransactions(anyString(), eq("2019-01-01"), eq("2019-12-01")))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "2019-01-01")
                                .param("toAccountingDate", "2019-12-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountTransactions> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountTransactions>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertEquals(1, response.getPayload().getList().length);
        Assertions.assertTrue(response.getPayload().getList()[0].getTransactionId().equalsIgnoreCase("123456"));
    }

    @Test
    void getAccountTransactions_testEmptyTransactions_200() throws Exception {
        Response<AccountTransactions> responseMock = new Response<>();
        AccountTransactions accountTransactions = new AccountTransactions();
        accountTransactions.setList(new AccountTransaction[]{});

        responseMock.setStatus(Status.OK);
        responseMock.setPayload(accountTransactions);

        ResponseEntity<Response<AccountTransactions>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.OK);

        when(accountTransactionService.getAccountTransactions(anyString(), eq("1900-01-01"), eq("1900-01-02")))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "1900-01-01")
                                .param("toAccountingDate", "1900-01-02"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountTransactions> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountTransactions>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertEquals(0, response.getPayload().getList().length);
    }

    @Test
    void getAccountTransactions_testInvalidAccountId_403() throws Exception {
        Response<AccountTransactions> responseMock = new Response<>();
        Error errorMock = new Error();

        errorMock.setCode("REQ004");
        errorMock.setDescription("Invalid account identifier");

        responseMock.setStatus(Status.KO);
        responseMock.setErrors(new Error[]{errorMock});

        ResponseEntity<Response<AccountTransactions>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.FORBIDDEN);
        when(accountTransactionService.getAccountTransactions(anyString(), eq("2019-01-01"), eq("2019-12-01")))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                        get("/{accountId}/transactions", "1")
                                .param("fromAccountingDate", "2019-01-01")
                                .param("toAccountingDate", "2019-12-01"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountTransactions> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountTransactions>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("REQ004", response.getErrors()[0].getCode());
        Assertions.assertEquals("Invalid account identifier", response.getErrors()[0].getDescription());
    }

    @Test
    void getAccountTransactions_testInvalidDate_400() throws Exception {
        Response<AccountTransactions> responseMock = new Response<>();
        Error errorMock = new Error();

        errorMock.setCode("REQ017");
        errorMock.setDescription("Invalid date format");

        responseMock.setStatus(Status.KO);
        responseMock.setErrors(new Error[]{errorMock});

        ResponseEntity<Response<AccountTransactions>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.BAD_REQUEST);

        when(accountTransactionService.getAccountTransactions(anyString(), eq("3000-01-01"), eq("3500-12-01")))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "3000-01-01")
                                .param("toAccountingDate", "3500-12-01"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountTransactions> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountTransactions>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("REQ017", response.getErrors()[0].getCode());
        Assertions.assertEquals("Invalid date format", response.getErrors()[0].getDescription());
    }
}
