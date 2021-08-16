package com.demo.fabrick.repository;

import com.demo.fabrick.entity.AccountTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionsRepository extends CrudRepository<AccountTransaction, Long> {
}