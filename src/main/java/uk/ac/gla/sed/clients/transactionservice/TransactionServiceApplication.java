package uk.ac.gla.sed.clients.transactionservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.transactionservice.core.EventProcessor;
import uk.ac.gla.sed.clients.transactionservice.core.ReceiptProcessor;
import uk.ac.gla.sed.clients.transactionservice.health.EventBusHealthCheck;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.clients.transactionservice.resources.HelloResource;
import uk.ac.gla.sed.clients.transactionservice.resources.StatusResource;
import uk.ac.gla.sed.clients.transactionservice.resources.apiTransactionResource;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

public class TransactionServiceApplication extends Application<TransactionServiceConfiguration> {


    public static void main(final String[] args) throws Exception {
        new TransactionServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "apiTransaction Service";
    }

    @Override
    public void initialize(final Bootstrap<TransactionServiceConfiguration> bootstrap) {

    }

    @Override
    public void run(final TransactionServiceConfiguration configuration,
                    final Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        
        final TransactionDAO dao = jdbi.onDemand(TransactionDAO.class);
        dao.deleteTableIfExists();
        dao.createTransactionTable();
        dao.addTransaction(1);
                
        final Integer lastTransactionID = dao.getHighestTransactionId();
        String eventBusURL = configuration.getEventBusURL();

        /* HEALTH CHECKS */
        final EventBusHealthCheck eventBusHealthCheck = new EventBusHealthCheck(eventBusURL);
        environment.healthChecks().register("event-bus", eventBusHealthCheck);
        // postgres is automatically checked

        /* MANAGED LIFECYCLES */
        final EventProcessor eventProcessor = new EventProcessor(
                eventBusURL,
                dao,
                environment.lifecycle().executorService("eventproessor").build()
        );
        EventBusClient eventBusClient = eventProcessor.getEventBusClient();
        final ReceiptProcessor receiptProcessor = new ReceiptProcessor(
                eventBusClient,
                dao,
                environment.lifecycle().executorService("receiptprocessor").build()
        );
        environment.lifecycle().manage(eventProcessor);
        environment.lifecycle().manage(receiptProcessor);

        environment.jersey().register(new HelloResource());
        environment.jersey().register(new apiTransactionResource(eventBusClient, lastTransactionID ,dao));
        environment.jersey().register(new StatusResource(dao));
    }

}
