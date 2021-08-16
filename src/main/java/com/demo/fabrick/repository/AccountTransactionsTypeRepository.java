package com.demo.fabrick.repository;

import com.demo.fabrick.entity.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionsTypeRepository extends CrudRepository<Type, Long> {

    boolean existsByValue(String value);
    Type findByValue(String value);
}