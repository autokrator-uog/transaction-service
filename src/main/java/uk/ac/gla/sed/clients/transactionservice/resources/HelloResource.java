package uk.ac.gla.sed.clients.transactionservice.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.transactionservice.api.Hello;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    @GET
    @Timed
    public Hello getHome() {return new Hello();}
}
