package uk.ac.gla.sed.clients.transactionservice.core.events;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import uk.ac.gla.sed.clients.transactionservice.api.apiTransaction;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

import java.math.BigDecimal;

public class PendingTransaction extends Event {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;

public PendingTransaction(Event e) throws IllegalArgumentException {
        super(e.getType(), Json.object().asObject().merge(e.getData()));

        if (!type.equals("PendingTransaction")) {
            throw new IllegalArgumentException("Event must be a PendingTransaction...");
        }

        this.transactionId =    data.getString("TransactionID", "");
        this.fromAccountId = data.getString("FromAccountID", "-1");
        this.toAccountId = data.getString("ToAccountID", "-1");
        this.amount = new BigDecimal(data.getString("Amount", "0"));
    }

    public PendingTransaction(apiTransaction transaction){
        super("PendingTransaction", Json.object().asObject());

        data.set("TransactionID", transaction.getTransactionID().toString());
        data.set("FromAccountID", transaction.getFromAccountID().toString());
        data.set("ToAccountID", transaction.getToAccountID().toString());
        data.set("Amount", transaction.getAmount().toString());
        }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
