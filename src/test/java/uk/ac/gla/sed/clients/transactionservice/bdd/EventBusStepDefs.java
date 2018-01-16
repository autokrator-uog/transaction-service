package uk.ac.gla.sed.clients.transactionservice.bdd;

import com.eclipsesource.json.Json;
import cucumber.api.java.Before;
import cucumber.api.java8.En;
import org.mockito.ArgumentCaptor;
import org.skife.jdbi.v2.DBI;
import uk.ac.gla.sed.clients.transactionservice.api.apiTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.EventProcessor;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.clients.transactionservice.resources.apiTransactionResource;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SuppressWarnings("ConstantConditions")
public class EventBusStepDefs implements En {
    private ExecutorService es;
    private EventProcessor eventProcessor;

    private DbTestFixture dbTestFixture;
    private DBI dbi;
    private TransactionDAO dao;
    private EventBusClient mockedEventBusClient;

    private apiTransactionResource apiTransactionResource;

    @Before
    public void setUp() throws Throwable {
        es = Executors.newSingleThreadExecutor();

        dbTestFixture = new DbTestFixture();
        dbTestFixture.before();
        dbi = dbTestFixture.getDbi();
        dao = dbi.onDemand(TransactionDAO.class);

        mockedEventBusClient = mock(EventBusClient.class);
        eventProcessor = new EventProcessor(mockedEventBusClient, dao, es);

        apiTransactionResource = new apiTransactionResource(mockedEventBusClient, 0, dao);
    }

    private void runEventProcessor() {
        try {
            eventProcessor.start();
            es.awaitTermination(5, TimeUnit.SECONDS);
            eventProcessor.stop();
        }
        catch (Exception e) {
            fail(e);
        }
    }

    private void setupReceiveEvent(Event e) {
        BlockingQueue<Event> bq = new LinkedBlockingQueue<>();
        bq.add(e);
        when(mockedEventBusClient.getIncomingEventsQueue()).thenReturn(bq);
    }

    private Event getProducedEventOrFail(String eventType) {
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(this.mockedEventBusClient, atLeastOnce()).sendEvent(eventCaptor.capture(), null);

        for (Event val : eventCaptor.getAllValues()) {
            if (val.getType().equals(eventType)) {
                return val;
            }
        }

        fail("Event not produced...");
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public EventBusStepDefs() {
        When("a[n]* \"(\\w+)\" event is received for transactionId (\\d+)", (String eventType, Integer transactionId) -> {
            Event event = new Event(eventType, Json.object().asObject()
                    .set("TransactionID", transactionId.toString())
            );
            setupReceiveEvent(event);
            runEventProcessor();
        });

        When("the user with accountId (\\d+) submits a transaction request to send £([\\d+.]+) to accountId (\\d+)", (Integer from, String amount, Integer to) -> {
            apiTransaction transaction = new apiTransaction();
            transaction.setFromAccountID(from);
            transaction.setToAccountID(to);
            transaction.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));

            apiTransactionResource.newTransaction(transaction);
        });

        Then("a PendingTransaction event was created from account (\\d+) to (\\d+) for amount £([\\d+.]+) with transactionId (\\d+)",
                (Integer from, Integer to, String amount, Integer transactionId) -> {
            Event producedEvent = getProducedEventOrFail("PendingTransaction");

            assertEquals(from.intValue(), producedEvent.getData().getInt("FromAccountID", -1));
            assertEquals(to.intValue(), producedEvent.getData().getInt("ToAccountID", -1));

            assertEquals(
                    transactionId.intValue(),
                    Integer.parseInt(producedEvent.getData().getString("TransactionID", "-1"))
            );

            BigDecimal expectedAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
            BigDecimal actualAmount = new BigDecimal(producedEvent.getData().getString("Amount", "-1")).setScale(2, RoundingMode.HALF_UP);
            assertEquals(expectedAmount, actualAmount);
        });
    }
}
