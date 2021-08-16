package com.demo.fabrick.controller;

import com.demo.fabrick.model.Response;
import com.demo.fabrick.model.accountBalance.AccountBalance;
import com.demo.fabrick.service.AccountBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountBalanceController {

    final
    AccountBalanceService balanceService;

    public AccountBalanceController(AccountBalanceService balanceService) {
        this.balanceService = balanceService;
    }


    @GetMapping("{accountId}/balance")
    public ResponseEntity<Response<AccountBalance>> getAccountBalance(@PathVariable String accountId) {
        return balanceService.getBalance(accountId);
    }
}
