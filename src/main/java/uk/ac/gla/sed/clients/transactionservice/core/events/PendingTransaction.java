package uk.ac.gla.sed.clients.transactionservice.core.events;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import uk.ac.gla.sed.clients.transactionservice.api.apiTransaction;
import uk.ac.gla.sed.shared.eventbusclient.api.Consistency;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;

import java.math.BigDecimal;

public class PendingTransaction extends Event {
    private String transactionId;
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal amount;

public PendingTransaction(Event e, Consistency consistency) throws IllegalArgumentException {
        super(e.getType(), Json.object().asObject().merge(e.getData()), consistency);

        if (!type.equals("PendingTransaction")) {
            throw new IllegalArgumentException("Event must be a PendingTransaction...");
        }

        this.transactionId =    data.getString("TransactionID", "");
        this.fromAccountId = data.getInt("FromAccountID", -1);
        this.toAccountId = data.getInt("ToAccountID", -1);
        this.amount = new BigDecimal(data.getString("Amount", "0"));
    }

    public PendingTransaction(apiTransaction transaction, Consistency consistency){
        super("PendingTransaction", Json.object().asObject(), consistency);

        data.set("TransactionID", transaction.getTransactionID().toString());
        data.set("FromAccountID", transaction.getFromAccountID());
        data.set("ToAccountID", transaction.getToAccountID());
        data.set("Amount", transaction.getAmount().toString());
        }

    public String getTransactionId() {
        return transactionId;
    }

    public Integer getFromAccountId() {
        return fromAccountId;
    }

    public Integer getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
