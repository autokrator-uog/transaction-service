package uk.ac.gla.sed.clients.transactionservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello{
    private String message = "This is the apiTransaction Service";

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
