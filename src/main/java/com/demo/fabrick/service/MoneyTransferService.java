package com.demo.fabrick.service;

import com.demo.fabrick.model.*;
import com.demo.fabrick.model.moneyTransfer.MoneyTransfer;
import com.demo.fabrick.model.moneyTransfer.request.MoneyTransferRequest;
import com.demo.fabrick.utility.ServiceUtility;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MoneyTransferService extends HttpActionService {

    public ResponseEntity<Response<MoneyTransfer>> postMoneyTransfer(String accountId, MoneyTransferRequest moneyTransfer) {
        return execPost(ServiceUtility.API_POST_MONEY_TRANSFER,
                new HttpEntity<>(moneyTransfer),
                new ParameterizedTypeReference<Response<MoneyTransfer>>() {
                },
                accountId
        );
    }
}