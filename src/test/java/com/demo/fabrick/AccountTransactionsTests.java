package com.demo.fabrick;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.accountTransaction.AccountTransactions;
import com.demo.fabrick.utility.JsonMapperUtility;
import com.demo.fabrick.utility.ServiceUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountTransactionsTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapperUtility jsonMapperUtility;

    @Test
    void getAccountTransactions_testSizeTransactions_200() throws Exception {
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
        Assertions.assertTrue(response.getPayload().getList().length > 0);
    }

    @Test
    void getAccountTransactions_testEmptyTransactions_200() throws Exception {
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

    @Test
    void getAccountTransactions_testInvertedDate_400() throws Exception {
        String json = mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "2020-01-01")
                                .param("toAccountingDate", "2019-12-01"))
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

    @Test
    void getAccountTransactions_testImpossibleDate_400() throws Exception {
        String json = mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "2019-02-29")
                                .param("toAccountingDate", "2019-12-01"))
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

    @Test
    void getAccountTransactions_testMandatoryRequestParam_400() throws Exception {
        mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("toAccountingDate", "2019-02-29"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAccountTransactions_testMandatoryRequestParam2_400() throws Exception {
        mockMvc.perform(
                        get("/{accountId}/transactions", ServiceUtility.ACCOUNT_ID)
                                .param("fromAccountingDate", "2019-02-29"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
