package com.demo.fabrick;

import com.demo.fabrick.model.Error;
import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.accountBalance.AccountBalance;
import com.demo.fabrick.service.AccountBalanceService;
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
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountBalanceMockTests {

    @Autowired
    private JsonMapperUtility jsonMapperUtility;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountBalanceService accountBalanceService;

    @Test
    void getBalance_mockTest_200() throws Exception {

        AccountBalance accountBalanceMock = new AccountBalance(LocalDate.now(), 123.12, 123.12, "EUR");
        Response<AccountBalance> responseMock = new Response<>();

        responseMock.setStatus(Status.OK);
        responseMock.setPayload(accountBalanceMock);

        ResponseEntity<Response<AccountBalance>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.OK);

        when(accountBalanceService.getBalance(anyString()))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                get("/{accountId}/balance", ServiceUtility.ACCOUNT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountBalance> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountBalance>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertEquals(123.12, response.getPayload().getBalance(), 0.0);
    }

    @Test
    void getBalance_mockTestInvalidAccountId_403() throws Exception {

        Response<AccountBalance> responseMock = new Response<>();
        Error errorMock = new Error();

        errorMock.setCode("REQ004");
        errorMock.setDescription("Invalid account identifier");

        responseMock.setStatus(Status.KO);
        responseMock.setErrors(new Error[]{errorMock});

        ResponseEntity<Response<AccountBalance>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.FORBIDDEN);

        when(accountBalanceService.getBalance(anyString()))
                .thenReturn(responseEntityMock);

        String json = mockMvc.perform(
                        get("/{accountId}/balance", "1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountBalance> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountBalance>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("REQ004", response.getErrors()[0].getCode());
        Assertions.assertEquals("Invalid account identifier", response.getErrors()[0].getDescription());
    }
}
