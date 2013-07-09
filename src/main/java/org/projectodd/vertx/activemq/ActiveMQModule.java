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
                if (event.succeeded()) {
                    startedResult.setResult(null);
                } else {
                    startedResult.setFailure( new Exception( "unable to start broker" ) );
                }
            }
        });
    }

    @Override
    public void stop() {
    }

}