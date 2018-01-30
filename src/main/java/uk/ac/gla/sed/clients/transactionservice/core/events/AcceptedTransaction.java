package uk.ac.gla.sed.clients.transactionservice.core.events;


import com.eclipsesource.json.Json;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

public class AcceptedTransaction extends Event {
    private String transactionId;

    public AcceptedTransaction(Event incomingEvent){
        super("AcceptedTransaction", Json.object().asObject(), incomingEvent.getConsistency());

        if (!incomingEvent.getType().equals("AcceptedTransaction")){
            throw new IllegalArgumentException("Event must be of type AcceptedTransaction");
        }
        this.type = incomingEvent.getType();

        data.merge(incomingEvent.getData());
        this.transactionId = data.getString("TransactionID", "-1");
    }

    public String getTransactionId() {
        return transactionId;
    }
}
