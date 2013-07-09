package org.projectodd.vertx.activemq.integration;

import org.junit.Test;
import org.projectodd.vertx.activemq.ActiveMQBrokerVerticle;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

public class AcitveMQModuleTest extends TestVerticle {

    @Override
    public void start() {
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                initialize();
                VertxAssert.assertTrue(asyncResult.succeeded());
                VertxAssert.assertNotNull("deploymentID should not be null", asyncResult.result());
                startTests();
            }
        });
    }

    @Test
    public void testBroker() throws InterruptedException {
        vertx.eventBus().send(ActiveMQBrokerVerticle.DEFAULT_ADDRESS, true, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                VertxAssert.assertEquals("tcp://localhost:61616", event.body().getString("url"));
                VertxAssert.testComplete();
            }
        });
    }

}
