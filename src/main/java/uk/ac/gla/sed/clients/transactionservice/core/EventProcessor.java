package uk.ac.gla.sed.clients.transactionservice.core;

import io.dropwizard.lifecycle.Managed;
import uk.ac.gla.sed.clients.transactionservice.core.events.AcceptedTransaction;
import uk.ac.gla.sed.clients.transactionservice.core.events.RejectedTransaction;
import uk.ac.gla.sed.clients.transactionservice.jdbi.TransactionDAO;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.concurrent.ExecutorService;

public class EventProcessor implements Managed {
    private final EventBusClient eventBusClient;
    private final ExecutorService workers;
    private final TransactionDAO dao;

    public  EventProcessor(String eventBusUrl, TransactionDAO dao, ExecutorService es){
        this.eventBusClient = new EventBusClient(eventBusUrl);
        this.workers = es;
        this.dao = dao;
    }

    @Override
    public void start() throws Exception{
        this.eventBusClient.start();
        // consume events
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
                    Event incomingEvent = eventBusClient.getIncomingEventsQueue().take();
                    if (incomingEvent.getType().equals("AcceptedTransaction")){
                        AcceptedTransaction accepeted = new AcceptedTransaction(incomingEvent);
                        // Process using transaction handler
                    }
                    if (incomingEvent.getType().equals("RejectedTransaction")){
                        RejectedTransaction rejected = new RejectedTransaction(incomingEvent);
                        // Process using transaction handler
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
