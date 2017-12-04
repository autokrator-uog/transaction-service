package uk.ac.gla.sed.clients.transactionservice;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.transactionservice.api.Transaction;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.clients.transactionservice.resources.HelloResource;
import uk.ac.gla.sed.clients.transactionservice.resources.TransactionResource;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class TransactionServiceApplication extends Application<TransactionServiceConfiguration> {

    public LinkedBlockingQueue<Transaction> incomingTransactions = new LinkedBlockingQueue<>();

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
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        final TransactionDAO dao = jdbi.onDemand(TransactionDAO.class);
        LinkedBlockingQueue<Transaction> toProcess = new LinkedBlockingQueue<>();


        System.out.println("What is your problem with me?");
        dao.deleteTableIfExists();
        dao.createTransactionTable();
        dao.addTransaction(1);
        dao.updateStatus(1,2,"fucked it");
        System.out.println(dao.getReason(1));
        System.out.println(dao.getStatus(1));



        environment.jersey().register(new HelloResource());
        environment.jersey().register(new TransactionResource(toProcess));
    }

}
