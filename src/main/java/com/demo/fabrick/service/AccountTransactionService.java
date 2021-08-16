package com.demo.fabrick.service;

import com.demo.fabrick.model.*;
import com.demo.fabrick.model.accountTransaction.AccountTransaction;
import com.demo.fabrick.model.accountTransaction.AccountTransactions;
import com.demo.fabrick.repository.AccountTransactionsRepository;
import com.demo.fabrick.repository.AccountTransactionsTypeRepository;
import com.demo.fabrick.utility.ModelMapperUtility;
import com.demo.fabrick.utility.ServiceUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AccountTransactionService extends HttpActionService {

    final
    ModelMapperUtility modelMapperUtility;

    final
    AccountTransactionsTypeRepository accountTransactionsTypeRepository;
    final
    AccountTransactionsRepository accountTransactionsRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AccountTransactionService(AccountTransactionsRepository accountTransactionsRepository,
                                     AccountTransactionsTypeRepository accountTransactionsTypeRepository, ModelMapperUtility modelMapperUtility) {
        this.accountTransactionsRepository = accountTransactionsRepository;
        this.accountTransactionsTypeRepository = accountTransactionsTypeRepository;
        this.modelMapperUtility = modelMapperUtility;
    }

    public ResponseEntity<Response<AccountTransactions>> getAccountTransactions(String accountId, String fromAccountingDate, String toAccountingDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ServiceUtility.API_GET_ACCOUNT_TRANSACTIONS)
                // Add query parameter
                .queryParam("fromAccountingDate", fromAccountingDate)
                .queryParam("toAccountingDate", toAccountingDate);

        ResponseEntity<Response<AccountTransactions>> response = execGet(
                builder.build().toUriString(),
                new HttpEntity<>(""),
                new ParameterizedTypeReference<Response<AccountTransactions>>() {},
                accountId
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            AccountTransaction[] jsonAccountTransactions = response.getBody().getPayload().getList();
            if (jsonAccountTransactions.length > 0) {
                logger.info("---> Account transactions:");
                // convert json obj to entity
                List<com.demo.fabrick.entity.AccountTransaction> listAccountTransactions =
                        Arrays.stream(jsonAccountTransactions)
                                .map(model -> modelMapperUtility.convertToEntity(model, com.demo.fabrick.entity.AccountTransaction.class))
                                .collect(Collectors.toList());
                // for every account transaction save/get the relative type
                listAccountTransactions.forEach(account -> {
                    if (accountTransactionsTypeRepository.existsByValue(account.getType().getValue())) {
                        account.setType(accountTransactionsTypeRepository.findByValue(account.getType().getValue()));
                    } else {
                        account.setType(accountTransactionsTypeRepository.save(account.getType()));
                    }
                    logger.info(account.toString());
                });
                // save all records to db
                accountTransactionsRepository.saveAll(listAccountTransactions);
            } else
                logger.info("---> No account transactions...");
        }
        return response;
    }
}