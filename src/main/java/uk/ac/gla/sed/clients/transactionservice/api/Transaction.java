package uk.ac.gla.sed.clients.transactionservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transaction {

    private Integer fromAccountID;

    private Integer toAccountID;

    private BigDecimal amount;

    private Integer transactionID;

    public Transaction() {}

    public Transaction(Integer fromAccountID, Integer toAccountID,
                        BigDecimal amount){
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        this.amount = amount;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty
    public Integer getFromAccountID() {
        return fromAccountID;
    }

    @JsonProperty
    public Integer getToAccountID() {
        return toAccountID;
    }

    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty("fromAccountID")
    public void setFromAccountID(Integer fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    @JsonProperty("toAccountID")
    public void setToAccountID(Integer toAccountID) {
        this.toAccountID = toAccountID;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
