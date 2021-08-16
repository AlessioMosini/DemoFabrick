package com.demo.fabrick;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.moneyTransfer.MoneyTransfer;
import com.demo.fabrick.model.moneyTransfer.request.MoneyTransferRequest;
import com.demo.fabrick.service.MoneyTransferService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferMockTests {

    @Autowired
    private JsonMapperUtility jsonMapperUtility;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MoneyTransferService moneyTransferService;

    @Test
    void postMoneyTransfer_testBodyResponse_200() throws Exception {
        Response<MoneyTransfer> responseMock = new Response<>();
        MoneyTransfer moneyTransferMock = new MoneyTransfer();
        moneyTransferMock.setMoneyTransferId("123");
        responseMock.setStatus(Status.OK);
        responseMock.setPayload(moneyTransferMock);

        ResponseEntity<Response<MoneyTransfer>> responseEntityMock = new ResponseEntity<>(responseMock, HttpStatus.OK);

        when(moneyTransferService.postMoneyTransfer(eq(ServiceUtility.ACCOUNT_ID), any(MoneyTransferRequest.class)))
                .thenReturn(responseEntityMock);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransferMock)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertFalse(response.getPayload().getMoneyTransferId().isEmpty());
    }
}
