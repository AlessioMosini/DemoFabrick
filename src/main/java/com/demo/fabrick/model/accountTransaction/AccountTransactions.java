package com.demo.fabrick.model.accountTransaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTransactions {
    @JsonProperty("list")
    private AccountTransaction[] list;

    public AccountTransaction[] getList() {
        return list;
    }

    public void setList(AccountTransaction[] list) {
        this.list = list;
    }
}
