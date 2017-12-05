package uk.ac.gla.sed.clients.transactionservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.transactionservice.api.apiTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.EventProcessor;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.clients.transactionservice.resources.HelloResource;
import uk.ac.gla.sed.clients.transactionservice.resources.apiTransactionResource;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.concurrent.LinkedBlockingQueue;

public class TransactionServiceApplication extends Application<TransactionServiceConfiguration> {

    public LinkedBlockingQueue<apiTransaction> incomingApiTransactions = new LinkedBlockingQueue<>();

    public static void main(final String[] args) throws Exception {
        new TransactionServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "apiTransaction Service";
    }

    @Override
    public void initialize(final Bootstrap<TransactionServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TransactionServiceConfiguration configuration,
                    final Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final TransactionDAO dao = jdbi.onDemand(TransactionDAO.class);
        final Integer lastTransactionID = dao.getHighestAccountId();
        String eventBusURL = configuration.getEventBusURL();



        dao.deleteTableIfExists();
        dao.createTransactionTable();

          /* MANAGED LIFECYCLES */
        final EventProcessor eventProcessor = new EventProcessor(
                eventBusURL,
                dao,
                environment.lifecycle().executorService("eventproessor").build()
        );
        EventBusClient eventBusClient = eventProcessor.getEventBusClient();
        environment.lifecycle().manage(eventProcessor);



        environment.jersey().register(new HelloResource());
        environment.jersey().register(new apiTransactionResource(eventBusClient, lastTransactionID ,dao));
    }

}
