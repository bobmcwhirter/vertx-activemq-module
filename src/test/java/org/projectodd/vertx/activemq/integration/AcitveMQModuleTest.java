package org.projectodd.vertx.activemq.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                initialize();
                startTests();
            }
        });
    }

    @Test
    public void testBroker() throws InterruptedException {
        vertx.eventBus().registerLocalHandler("my.queues.foo.handler", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                String body = event.body();
                VertxAssert.assertEquals( "howdy!", body );
                System.err.println( "HANDLED: " + body );
                event.reply(true);
                VertxAssert.testComplete();
            }
        });
        
        vertx.eventBus().send("org.projectodd.jms", new JsonObject().putString("subscribe", "/queues/foo").putString("address", "my.queues.foo.handler"), new Handler<Message<Boolean>>() {
            @Override
            public void handle(Message<Boolean> event) {
                vertx.eventBus().send( "org.projectodd.jms", new JsonObject().putString( "body", "howdy!" ).putString( "send", "/queues/foo" ) );
            }
        });

    }

}
