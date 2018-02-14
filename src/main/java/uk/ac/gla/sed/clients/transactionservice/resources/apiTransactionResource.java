package uk.ac.gla.sed.clients.transactionservice.resources;


import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.transactionservice.api.apiTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.events.PendingTransaction;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.Consistency;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.LinkedBlockingQueue;


@Path("/transaction")
@Consumes(MediaType.APPLICATION_JSON)
public class apiTransactionResource {

    private final EventBusClient eventBusClient;
    private Integer currentID;
    private TransactionDAO dao;

    public apiTransactionResource(EventBusClient eventBusClient, Integer lastUsedID, TransactionDAO dao){
        this.eventBusClient = eventBusClient;
        this.currentID = lastUsedID;
        this.dao = dao;
    }


    // TODO: Look at this returning a response acknowledging the transaction. (include the given id)

    @POST
    @Timed
    public apiTransaction newTransaction(apiTransaction incoming){
        if (currentID == null){
            currentID = 1;
        }
        currentID++;
        incoming.setTransactionID(currentID);

        String consistencyKey = String.format("TX-s%d-r%d", incoming.getFromAccountID(), incoming.getToAccountID());

        PendingTransaction current = new PendingTransaction(incoming, new Consistency(consistencyKey    , "*"));

        dao.addTransaction(currentID);
        dao.updateStatus(currentID,0);
        eventBusClient.sendEvent(current, null);


        return incoming;
    }
}
