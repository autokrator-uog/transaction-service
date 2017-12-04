package uk.ac.gla.sed.clients.transactionservice.core.events;

import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class RejectedTransaction extends Event {
    private String transactionId;
    private String reason;

    public RejectedTransaction(PendingTransaction transaction, String reason) {
        this.transactionId = transaction.getTransactionId();
        this.reason = reason;

        this.type = "RejectedTransaction";

        this.data = Json.object().asObject();
        this.data.set("TransactionID", this.transactionId);
        this.data.set("Reason", this.reason);
    }

    public RejectedTransaction(Event incomingEvent){
        if (!incomingEvent.getType().equals("RejectedTransaction")){
            throw new IllegalArgumentException("Event must be of type RejectedTransaction");
        }
        this.type = incomingEvent.getType();

        this.data = Json.object().asObject();
        this.data.merge(incomingEvent.getData());
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
