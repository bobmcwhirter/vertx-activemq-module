package org.projectodd.vertx.activemq;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.vertx.java.core.Future;
import org.vertx.java.platform.Verticle;

public class ActiveMQBrokerVerticle extends Verticle {

    private String brokerUrl;
    private BrokerService broker;

    @Override
    public void start(Future<Void> startedResult) {
        System.err.println("============================ BROKER START");

        this.brokerUrl = "broker:tcp://localhost:61616";
        try {
            this.broker = BrokerFactory.createBroker(brokerUrl, true);
        } catch (Exception e) {
            e.printStackTrace();
            startedResult.setFailure(e);
            return;
        }

        System.err.println("============================ BROKER STARTED");
        startedResult.setResult(null);
    }

    @Override
    public void stop() {
        try {
            System.err.println("============================ BROKER STOP");
            this.broker.stop();
            this.broker = null;
            System.err.println("============================ BROKER STOPPED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
