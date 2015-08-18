package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

/**
 * Local, in-memory DynamoDB simulator.
 *
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html
 */
public class DynamoDbLocal {
    private final DynamoDBProxyServer server;
    private final String port;

    public static void main(String[] args) throws Exception {
        new DynamoDbLocal().start();
    }

    public DynamoDbLocal() throws Exception {
        configureSqlite();
        port = String.valueOf(PortResolver.findOpenPort());
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-sharedDb", "-inMemory", "-port", port});
        ensureDynamoDbIsStoppedOnShutdown();
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public String getPort() {
        return port;
    }

    /**
     * This is ugly! We have to tell SQLite4Java where the native sqlite libraries are located.
     * Our pom.xml file ensures they are copied from your local maven repository (~/.m2/repository)
     * to target/lib on compilation.
     *
     * See this site for further details: https://code.google.com/p/sqlite4java/wiki/UsingWithMaven
     */
    private void configureSqlite() {
        System.setProperty("sqlite4java.library.path", "target/lib");
    }

    private void ensureDynamoDbIsStoppedOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println("Exception when stopping DynamoDB Local: " + e.toString());
            }
        }));
    }
}
