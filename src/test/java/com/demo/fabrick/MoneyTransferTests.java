package com.demo.fabrick;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.Status;
import com.demo.fabrick.model.moneyTransfer.Account;
import com.demo.fabrick.model.moneyTransfer.Creditor;
import com.demo.fabrick.model.moneyTransfer.MoneyTransfer;
import com.demo.fabrick.model.moneyTransfer.request.MoneyTransferRequest;
import com.demo.fabrick.service.AccountBalanceService;
import com.demo.fabrick.utility.JsonMapperUtility;
import com.demo.fabrick.utility.LocalDateUtility;
import com.demo.fabrick.utility.ServiceUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferTests {

    @Autowired
    private JsonMapperUtility jsonMapperUtility;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountBalanceService accountBalanceService;
    @Autowired
    private LocalDateUtility localDateUtility;

    @Test
    void postMoneyTransfer_testBodyResponse_200() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFirstWorkingDay(),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertFalse(response.getPayload().getMoneyTransferId().isEmpty());
        Assertions.assertEquals("WORK_IN_PROGRESS", response.getPayload().getStatus());
    }

    @Test
    void postMoneyTransfer_testBookedTransaction_200() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFutureWorkingDay(30),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.OK, response.getStatus());
        Assertions.assertFalse(response.getPayload().getMoneyTransferId().isEmpty());
        Assertions.assertEquals("BOOKED", response.getPayload().getStatus());
    }

    @Test
    void postMoneyTransfer_testInvalidAccountCode_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT23A0336844430152923804660")),
                localDateUtility.getFirstWorkingDay(),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("Coordinate IBAN errate o non aggiornate, non è possibile ricavare un BIC valido. Contattare il beneficiario per ottenere le coordinate IBAN aggiornate.", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testPastDate_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFirstPastWorkingDay(),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("DataEsecuzioneOrdine non valida, deve essere maggiore o uguale al primo giorno lavorativo disponibile per la lavorazione e non superiore a 30 gg lavorativi.", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testNotWorkingDate_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFirstNotWorkingDay(),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("Errore nel recuperare le informazioni da database o dati di input non corretti  : IBTS-0081", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testFutureDate_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFutureWorkingDay(31),
                "donazione",
                "EUR",
                0.01);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("DataEsecuzioneOrdine non valida, deve essere maggiore o uguale al primo giorno lavorativo disponibile per la lavorazione e non superiore a 30 gg lavorativi.", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testInvalidAccountId_403() throws Exception {
        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(new MoneyTransferRequest())))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("REQ004", response.getErrors()[0].getCode());
        Assertions.assertEquals("Invalid account identifier", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testAmountMoreThenBalanceAvailable_400() throws Exception {
        int availableBalance =
                accountBalanceService.getBalance(ServiceUtility.ACCOUNT_ID).getBody().getPayload().getAvailableBalance().intValue();

        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFirstWorkingDay(),
                "donazione",
                "EUR",
                Math.abs(availableBalance)+1.0);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("Fondi non sufficienti", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testExceedLimitAmount_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFutureWorkingDay(1),
                "donazione",
                "EUR",
                9999999.0);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("REQ022", response.getErrors()[0].getCode());
        Assertions.assertEquals("Invalid Amount: Transaction Limit Exceeding", response.getErrors()[0].getDescription());
    }

    @Test
    void postMoneyTransfer_testInvalidFormatAmount_400() throws Exception {
        MoneyTransferRequest moneyTransfer = new MoneyTransferRequest(
                new Creditor("Benefattore", new Account("IT60X0542811101000000123456")),
                localDateUtility.getFirstWorkingDay(),
                "donazione",
                "EUR",
                1.99999999);

        String json = this.mockMvc.perform(
                        post("/{accountId}/payments/money-transfers", ServiceUtility.ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapperUtility.convertObjectToJson(moneyTransfer)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Response<MoneyTransfer> response = jsonMapperUtility.convertJsonToObject(json, new TypeReference<Response<MoneyTransfer>>() {});

        Assertions.assertEquals(Status.KO, response.getStatus());
        Assertions.assertEquals("API000", response.getErrors()[0].getCode());
        Assertions.assertEquals("Il campo ValoreImporto è formalmente errato", response.getErrors()[0].getDescription());
    }


}
