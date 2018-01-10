package uk.ac.gla.sed.clients.transactionservice.bdd;

import cucumber.api.java.Before;
import cucumber.api.java8.En;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.transactionservice.core.handlers.TransactionHandler;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TransactionStepDefs implements En {
    private DbTestFixture db;
    private DBI dbi;

    @Before
    public void setUpdDBFixture() throws Throwable {
        db = new DbTestFixture();
        db.before();
        dbi = db.getDbi();
    }

    public TransactionStepDefs() {

        Given("there exists a PendingTransaction with id (\\d+)", (Integer transactionId) -> {
            final TransactionDAO dao = dbi.onDemand(TransactionDAO.class);
            dao.addTransaction(transactionId);
        });

        Then("the transaction service has stored a transaction with id (\\d+) with status \"(\\w+)\"", (Integer transactionId, String status) -> {
            final TransactionDAO dao = dbi.onDemand(TransactionDAO.class);
            Integer statusActual = dao.getStatus(transactionId);

            switch (status) {
                case "PENDING":
                    assertEquals(0, statusActual.intValue());
                    break;
                case "ACCEPTED":
                    assertEquals(TransactionHandler.STATUS_ACCEPTED, statusActual.intValue());
                    break;
                case "REJECTED":
                    assertEquals(TransactionHandler.STATUS_REJECTED, statusActual.intValue());
                    break;
            }
        });

    }
}
