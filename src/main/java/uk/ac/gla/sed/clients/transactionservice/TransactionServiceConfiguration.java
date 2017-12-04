package uk.ac.gla.sed.clients.transactionservice;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
