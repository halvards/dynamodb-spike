package com.skogsrud.halvard.dynamodb.spike;

import com.almworks.sqlite4java.SQLite;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class DynamoDbTest {
    private static String port;
    private static DynamoDBProxyServer server;

    @BeforeClass
    public static void beforeAll() throws Exception {
        // Aweful! https://code.google.com/p/sqlite4java/wiki/UsingWithMaven
        SQLite.setLibraryPath("target/lib");
//        SQLite.setLibraryPath(System.getenv("HOME") + "/.m2/repository/com/almworks/sqlite4java/libsqlite4java-osx/1.0.392");
        port = String.valueOf(findOpenPort());
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", port});
        server.start();
    }

    @AfterClass
    public static void afterAll() throws Exception {
        server.stop();
    }

    @Test
    public void test() throws Exception {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setEndpoint("http://localhost:" + port);
        ListTablesResult listTablesResult = client.listTables();
        System.out.println("listTablesResult = " + listTablesResult);
    }

    public static int findOpenPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
