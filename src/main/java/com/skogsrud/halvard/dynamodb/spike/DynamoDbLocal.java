package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

public class DynamoDbLocal {
    private final DynamoDBProxyServer server;
    private final String port;

    public static void main(String[] args) throws Exception {
        new DynamoDbLocal().run();
    }

    public DynamoDbLocal() throws Exception {
        System.setProperty("sqlite4java.library.path", "target/lib");
        port = String.valueOf(PortResolver.findOpenPort());
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", port});
    }

    public void run() throws Exception {
        server.start();
        ensureDynamoDbIsStoppedOnShutdown();
    }

    public String getPort() {
        return port;
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
