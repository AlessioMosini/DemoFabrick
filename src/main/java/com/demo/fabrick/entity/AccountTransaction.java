package com.demo.fabrick.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class AccountTransaction {

    @Id
    private String transactionId;
    private String operationId;
    @Column(name = "valueDate", columnDefinition = "DATE")
    private LocalDate valueDate;
    @Column(name = "accountingDate", columnDefinition = "DATE")
    private LocalDate accountingDate;
    private Double amount;
    private String currency;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type", referencedColumnName = "id")
    private Type type;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(LocalDate accountingDate) {
        this.accountingDate = accountingDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", operationId='" + operationId + '\'' +
                ", valueDate=" + valueDate +
                ", accountingDate=" + accountingDate +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}