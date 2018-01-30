package uk.ac.gla.sed.clients.transactionservice.core.events;

import com.eclipsesource.json.Json;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class RejectedTransaction extends Event {
    private String transactionId;
    private String reason;

    public RejectedTransaction(Event incomingEvent){
        super("RejectedTransaction", Json.object().asObject(), incomingEvent.getConsistency());
        if (!incomingEvent.getType().equals("RejectedTransaction")){
            throw new IllegalArgumentException("Event must be of type RejectedTransaction");
        }

        data.merge(incomingEvent.getData());
        this.transactionId = data.getString("TransactionID", "");
        this.reason = data.getString("Reason", "Reason not found");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getReason() {
        return reason;
    }
}
