package com.demo.fabrick.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Type {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String enumeration;
    private String value;
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<AccountTransaction> accountTransactionsList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<AccountTransaction> getAccountTransactionsList() {
        return accountTransactionsList;
    }

    public void setAccountTransactionsList(List<AccountTransaction> accountTransactionsList) {
        this.accountTransactionsList = accountTransactionsList;
    }
}