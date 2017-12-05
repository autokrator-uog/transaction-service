package uk.ac.gla.sed.clients.transactionservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Status {

    private Integer transactionID;
    private Integer status;
    private String reason;

    public Status(Integer transactionID, Integer status, String reason) {
        this.transactionID = transactionID;
        this.status = status;
        this.reason = reason;
    }

    public Status(Integer transactionID, Integer status) {
        this.transactionID = transactionID;
        this.status = status;
    }

    @JsonProperty
    public Integer getTransactionID() {
        return transactionID;
    }

    @JsonProperty
    public Integer getStatus() {
        return status;
    }

    @JsonProperty
    public String getReason() {
        return reason;
    }
}


