package com.demo.fabrick.controller;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.accountTransaction.AccountTransactions;
import com.demo.fabrick.service.AccountTransactionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountTransactionsController {

    final
    AccountTransactionService accountTransactionService;

    public AccountTransactionsController(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }

    @GetMapping("{accountId}/transactions")
    public ResponseEntity<Response<AccountTransactions>> getAccountBalance(
            @PathVariable String accountId,
            @RequestParam String fromAccountingDate,
            @RequestParam String toAccountingDate) {
        ResponseEntity<Response<AccountTransactions>> response =
                accountTransactionService.getAccountTransactions(accountId, fromAccountingDate, toAccountingDate);

        // create new header to override response header -> connection = close
        HttpHeaders httpHeaders = new HttpHeaders();
        response.getHeaders().forEach((key, patterns) -> patterns.forEach(value -> httpHeaders.set(key, value)));
        httpHeaders.set("Keep-Alive", "timeout=15, max=100");
        httpHeaders.setConnection("Keep-Alive");

        return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatusCode());

    }
}
