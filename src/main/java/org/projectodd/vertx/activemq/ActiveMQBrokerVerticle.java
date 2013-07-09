package org.projectodd.vertx.activemq;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class ActiveMQBrokerVerticle extends Verticle {

    public static final String DEFAULT_ADDRESS = "org.projectodd.jms.broker";
    
    private BrokerService broker;

    @Override
    public void start(Future<Void> startedResult) {
        String brokerUrl = container.config().getString("url");
        if (brokerUrl == null) {
            brokerUrl = "broker:tcp://localhost:61616";
        }

        try {
            System.err.println( "CL: " + BrokerFactory.class.getClassLoader() );
            this.broker = BrokerFactory.createBroker(brokerUrl, true);
        } catch (Exception e) {
            e.printStackTrace();
            startedResult.setFailure(e);
            return;
        }
        
        String brokerAddress = container.config().getString("address");
        
        if ( brokerAddress == null ) {
            brokerAddress = DEFAULT_ADDRESS;
        }
        
        vertx.eventBus().registerHandler(brokerAddress, new Handler<Message<?>>() {
            @Override
            public void handle(Message<?> event) {
                event.reply( new JsonObject()
                    .putString("connection_creator_class", "org.projectodd.vertx.activemq.jmsclient.ActiveMQConnectionCreator") 
                    .putString("url", ActiveMQBrokerVerticle.this.broker.getDefaultSocketURIString() ) );
            }
        });
        
        startedResult.setResult(null);
    }

    @Override
    public void stop() {
        try {
            this.broker.stop();
            this.broker = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
