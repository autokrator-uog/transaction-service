package uk.ac.gla.sed.clients.resources;

import com.codahale.metrics.annotation.Timed;
import uk.ac.gla.sed.clients.api.Hello;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import java.awt.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    @GET
    @Timed
    public Hello getHome() {return new Hello();}
}
