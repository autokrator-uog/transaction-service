package uk.ac.gla.sed.clients.transactionservice.resources;


import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.transactionservice.TransactionServiceApplication;
import uk.ac.gla.sed.clients.transactionservice.api.Transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.LinkedBlockingQueue;


@Path("/transaction")
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final LinkedBlockingQueue<Transaction> toProcess;

    public TransactionResource(LinkedBlockingQueue<Transaction> toProcess){
        this.toProcess = toProcess;
    }

    int id = 0;

    @POST
    @Timed
    public Transaction newTransaction(Transaction incoming){
        id++;
        incoming.setTransactionID(id);
        toProcess.add(incoming);
        System.out.println(toProcess.toString());
        return incoming;
    }
}
