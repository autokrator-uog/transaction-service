package uk.ac.gla.sed.clients.transactionservice.core;

import io.dropwizard.lifecycle.Managed;
import uk.ac.gla.sed.shared.eventbusclient.api.Event;
import uk.ac.gla.sed.shared.eventbusclient.api.EventBusClient;

import java.util.concurrent.ExecutorService;

public class EventProcessor implements Managed {
    private final EventBusClient eventBusClient;
    private final ExecutorService workers;

    public  EventProcessor(String eventBusUrl, ExecutorService es){
        this.eventBusClient = new EventBusClient(eventBusUrl);
        this.workers = es;
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

                    }
                    if (incomingEvent.getType().equals("RejectedTransaction")){

                    }
                }
                catch (InterruptedException e){
                    System.out.println("ConsumeEventTast interrupted");
                    return;
                }
            }
        }
    }
}
