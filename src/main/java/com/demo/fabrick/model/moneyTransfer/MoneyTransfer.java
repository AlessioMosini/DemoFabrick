package com.demo.fabrick.model.moneyTransfer;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  MoneyTransfer {

    private String moneyTransferId;
    private String cro;
    private String trn;
    private String status;
    private String uri;
    private String direction;
    private Debtor debtor;
    private Creditor creditor;
    private String feeAccountID;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime createdDatetime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime accountedDatetime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate debtorValueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creditorValueDate;
    private Amount amount;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private Fees[] fees;
    private Boolean hasTaxRelief;



    public String getMoneyTransferId() {
        return moneyTransferId;
    }

    public void setMoneyTransferId(String moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public Creditor getCreditor() {
        return creditor;
    }

    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    public String getFeeAccountID() {
        return feeAccountID;
    }

    public void setFeeAccountID(String feeAccountID) {
        this.feeAccountID = feeAccountID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(OffsetDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public OffsetDateTime getAccountedDatetime() {
        return accountedDatetime;
    }

    public void setAccountedDatetime(OffsetDateTime accountedDatetime) {
        this.accountedDatetime = accountedDatetime;
    }

    public LocalDate getDebtorValueDate() {
        return debtorValueDate;
    }

    public void setDebtorValueDate(LocalDate debtorValueDate) {
        this.debtorValueDate = debtorValueDate;
    }

    public LocalDate getCreditorValueDate() {
        return creditorValueDate;
    }

    public void setCreditorValueDate(LocalDate creditorValueDate) {
        this.creditorValueDate = creditorValueDate;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public Boolean getInstant() {
        return isInstant;
    }

    public void setInstant(Boolean instant) {
        isInstant = instant;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Fees[] getFees() {
        return fees;
    }

    public void setFees(Fees[] fees) {
        this.fees = fees;
    }

    public Boolean getHasTaxRelief() {
        return hasTaxRelief;
    }

    public void setHasTaxRelief(Boolean hasTaxRelief) {
        this.hasTaxRelief = hasTaxRelief;
    }
}