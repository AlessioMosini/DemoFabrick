package com.demo.fabrick;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.accountBalance.AccountBalance;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountBalanceTests {

    @Autowired
    private JsonMapperUtility jsonMapperUtility;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBalance_testBalance_200() throws Exception {
        String json = mockMvc.perform(
                get("/{accountId}/balance", ServiceUtility.ACCOUNT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<AccountBalance> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<AccountBalance>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertFalse(response.getPayload().getBalance().isNaN());
    }

    @Test
    void getBalance_testInvalidAccountId_403() throws Exception {
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
