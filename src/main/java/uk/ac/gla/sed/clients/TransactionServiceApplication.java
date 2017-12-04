package uk.ac.gla.sed.clients;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.ac.gla.sed.clients.resources.HelloResource;

public class TransactionServiceApplication extends Application<TransactionServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TransactionServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Transaction Service";
    }

    @Override
    public void initialize(final Bootstrap<TransactionServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TransactionServiceConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new HelloResource());
    }

}
