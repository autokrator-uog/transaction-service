package uk.ac.gla.sed.clients.transactionservice.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.transactionservice.api.Status;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/status/{TransactionID}")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {
    private final TransactionDAO dao;

    public StatusResource(TransactionDAO dao) {
        this.dao = dao;
    }

    @GET
    @Timed
    public Status getStatusDetails(@PathParam("TransactionID") int TransactionId) {
        System.out.println(TransactionId);
        Integer status = dao.getStatus(TransactionId);
        String reason = dao.getReason(TransactionId);
        Status toStatus;

        if (status == 2){
            toStatus = new Status(TransactionId,status,reason);
        }else {
            toStatus = new Status(TransactionId,status);
        }
        return toStatus;
    }
}

