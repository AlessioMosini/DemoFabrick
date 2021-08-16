package com.demo.fabrick.controller;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.moneyTransfer.MoneyTransfer;
import com.demo.fabrick.model.moneyTransfer.request.MoneyTransferRequest;
import com.demo.fabrick.service.MoneyTransferService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MoneyTransferController {

    final
    MoneyTransferService moneyTransferService;

    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping("{accountId}/payments/money-transfers")
    public ResponseEntity<Response<MoneyTransfer>> getAccountBalance(
            @PathVariable String accountId,
            @RequestBody MoneyTransferRequest moneyTransfer) {

        ResponseEntity<Response<MoneyTransfer>> response = moneyTransferService.postMoneyTransfer(accountId, moneyTransfer);

        // create new header to override response header -> connection = close
        HttpHeaders httpHeaders = new HttpHeaders();
        response.getHeaders().forEach((key, patterns) -> patterns.forEach(value -> httpHeaders.set(key, value)));
        httpHeaders.set("Keep-Alive", "timeout=15, max=100");
        httpHeaders.setConnection("Keep-Alive");

        return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());
    }
}
