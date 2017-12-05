package uk.ac.gla.sed.clients.transactionservice.core.handlers;

import uk.ac.gla.sed.clients.transactionservice.core.events.AcceptedTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.events.RejectedTransaction;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.logging.Logger;

public class TransactionHandler {
    // TODO: LOGGING, DOCS, BASIC ERRORS?
    private static final Logger LOG = Logger.getLogger(TransactionHandler.class.getName());

    private static final int STATUS_ACCEPTED = 1;
    private static final int STATUS_REJECTED = 2;

    private TransactionDAO dao;
    private EventBusClient eventBusClient;

    public TransactionHandler(TransactionDAO dao){
        this.dao = dao;
    }

    public void handleTransaction(RejectedTransaction rejected){
        Integer id = Integer.parseInt(rejected.getTransactionId());
        String reason = rejected.getReason();

        dao.updateStatus(id, STATUS_REJECTED, reason);
        LOG.fine(String.format("Handled rejected transaction %s with reason: %s", rejected.getTransactionId(), rejected.getReason()));
        //System.out.println("Rejected handled");
    }

    public void handleTransaction(AcceptedTransaction accepted){
        Integer id = Integer.parseInt(accepted.getTransactionId());

        dao.updateStatus(id, STATUS_ACCEPTED);
        LOG.fine(String.format("Handled accepted transaction %s", accepted.getTransactionId()));
        //System.out.println("Accepted handled");
    }
}
