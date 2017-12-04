package uk.ac.gla.sed.clients.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello{
    private String message = "This is the Transaction Service";

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
