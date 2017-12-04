package uk.ac.gla.sed.clients;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

public class TransactionServiceConfiguration extends Configuration {
    @NotEmpty
    private String eventBusURL;

    @JsonProperty
    public String getEventBusURL() {
        return eventBusURL;
    }

    @JsonProperty
    public void setEventBusURL(String eventBusURL) {
        this.eventBusURL = eventBusURL;
    }
}
