package uk.ac.gla.sed.clients.transactionservice.core;

import com.google.common.eventbus.EventBus;
import io.dropwizard.lifecycle.Managed;
import uk.ac.gla.sed.clients.transactionservice.core.events.AcceptedTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.events.RejectedTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.handlers.TransactionHandler;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;
import uk.ac.gla.sed.shared.eventbusclient.internal.messages.RegisterMessage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class EventProcessor implements Managed {
    private final EventBusClient eventBusClient;
    private final ExecutorService workers;
    private final TransactionDAO dao;

    public EventProcessor(String eventBusUrl, TransactionDAO dao, ExecutorService es){
        this(new EventBusClient(eventBusUrl), dao, es);
    }

    public EventProcessor(EventBusClient eventBusClient, TransactionDAO dao, ExecutorService es) {
        this.eventBusClient = eventBusClient;
        this.dao = dao;
        this.workers = es;
    }

    @Override
    public void start() throws Exception{
        this.eventBusClient.start();
        ArrayList<String> interestedEvents = new ArrayList<>();
        interestedEvents.add("AcceptedTransaction");
        interestedEvents.add("RejectedTransaction");
        RegisterMessage registration = new RegisterMessage("transaction", interestedEvents);
        eventBusClient.register(registration);
        workers.submit(new ConsumeEventTask());
    }

    public EventBusClient getEventBusClient(){
        return this.eventBusClient;
    }

    @Override
    public void stop(){
        this.eventBusClient.stop();
    }

    class ConsumeEventTask implements Runnable {
        @Override
        public void run(){
            while(true){
                try{
                    TransactionHandler handler = new TransactionHandler(dao);
                    Event incomingEvent = eventBusClient.getIncomingEventsQueue().take();
                    if (incomingEvent.getType().equals("AcceptedTransaction")){
                        AcceptedTransaction accepted = new AcceptedTransaction(incomingEvent);
                        handler.handleTransaction(accepted);
                    }
                    if (incomingEvent.getType().equals("RejectedTransaction")){
                        RejectedTransaction rejected = new RejectedTransaction(incomingEvent);
                        handler.handleTransaction(rejected);
                    }
                }
                catch (InterruptedException e){
                    System.out.println("ConsumeEventTask interrupted");
                    return;
                }
            }
        }
    }
}
