package uk.ac.gla.sed.clients.transactionservice.resources;


import uk.ac.gla.sed.clients.transactionservice.TransactionServiceApplication;
import uk.ac.gla.sed.clients.transactionservice.api.Transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


@Path("/transaction")
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {


    @POST
    public Transaction newTransaction(Transaction incoming){

        return incoming;
    }
}
