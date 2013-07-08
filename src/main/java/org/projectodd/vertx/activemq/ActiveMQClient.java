package org.projectodd.vertx.activemq;

import java.util.concurrent.atomic.AtomicInteger;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

public class ActiveMQClient {
    
    private Vertx vertx;
    private static AtomicInteger counter = new AtomicInteger();

    public ActiveMQClient(Vertx vertx) {
        this.vertx = vertx;
    }
    
    public void subscribe(String destination, Handler<Message<String>> handler) {
        String address = "org.projectodd.activemq." + counter.getAndIncrement();
        vertx.eventBus().registerLocalHandler(address, handler );
        
        vertx.eventBus().send("org.projectodd.jms", new JsonObject()
            .putString("subscribe", destination)
            .putString("address", address) );
    }

}
