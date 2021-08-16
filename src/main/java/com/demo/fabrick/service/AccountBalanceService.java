package com.demo.fabrick.service;

import com.demo.fabrick.model.accountBalance.AccountBalance;
import com.demo.fabrick.model.Response;
import com.demo.fabrick.utility.ServiceUtility;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AccountBalanceService extends HttpActionService {

    public ResponseEntity<Response<AccountBalance>> getBalance(String accountId) {
        return execGet(
                ServiceUtility.API_GET_ACCOUNT_BALANCE,
                new HttpEntity<>(""),
                new ParameterizedTypeReference<Response<AccountBalance>>() {},
                accountId
        );
    }
}
