package org.projectodd.vertx.activemq;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;

public class ActiveMQModule extends Verticle {

    @Override
    public void start(final Future<Void> startedResult) {
        container.deployVerticle("org.projectodd.vertx.activemq.ActiveMQBrokerVerticle", new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                container.deployVerticle("org.projectodd.vertx.activemq.ActiveMQConnectionVerticle", new Handler<AsyncResult<String>>() {
                    @Override
                    public void handle(AsyncResult<String> event) {
                        startedResult.setResult(null);
                    }
                });
            }
        });
    }

    @Override
    public void stop() {
    }

}