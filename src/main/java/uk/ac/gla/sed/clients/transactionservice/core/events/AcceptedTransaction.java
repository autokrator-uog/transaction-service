package uk.ac.gla.sed.clients.transactionservice.core.events;


import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AcceptedTransaction extends Event {
    private String transactionId;

    public AcceptedTransaction(PendingTransaction transaction) {
        this.transactionId = transaction.getTransactionId();

        this.type = "AcceptedTransaction";

        this.data = Json.object().asObject();
        this.data.set("TransactionID", this.transactionId);
    }

    public AcceptedTransaction(Event incomingEvent){
        if (!incomingEvent.getType().equals("AcceptedTransaction")){
            throw new IllegalArgumentException("Event must be of type AcceptedTransaction");
        }
        this.type = incomingEvent.getType();

        this.data = Json.object().asObject();
        this.data.merge(incomingEvent.getData());
        this.transactionId = data.getString("TransactionID", "");
    }

    public String getTransactionId() {
        return transactionId;
    }
}
