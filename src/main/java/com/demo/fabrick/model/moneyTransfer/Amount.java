package com.demo.fabrick.model.moneyTransfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Amount {

    private Double debtorAmount;
    private String debtorCurrency;
    private Double creditorAmount;
    private String creditorCurrency;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creditorCurrencyDate;
    private Double currencyRatio;

    public Double getDebtorAmount() {
        return debtorAmount;
    }

    public void setDebtorAmount(Double debtorAmount) {
        this.debtorAmount = debtorAmount;
    }

    public String getDebtorCurrency() {
        return debtorCurrency;
    }

    public void setDebtorCurrency(String debtorCurrency) {
        this.debtorCurrency = debtorCurrency;
    }

    public Double getCreditorAmount() {
        return creditorAmount;
    }

    public void setCreditorAmount(Double creditorAmount) {
        this.creditorAmount = creditorAmount;
    }

    public String getCreditorCurrency() {
        return creditorCurrency;
    }

    public void setCreditorCurrency(String creditorCurrency) {
        this.creditorCurrency = creditorCurrency;
    }

    public LocalDate getCreditorCurrencyDate() {
        return creditorCurrencyDate;
    }

    public void setCreditorCurrencyDate(LocalDate creditorCurrencyDate) {
        this.creditorCurrencyDate = creditorCurrencyDate;
    }

    public Double getCurrencyRatio() {
        return currencyRatio;
    }

    public void setCurrencyRatio(Double currencyRatio) {
        this.currencyRatio = currencyRatio;
    }
}