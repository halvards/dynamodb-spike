package com.skogsrud.halvard.dynamodb.spike;

import com.almworks.sqlite4java.SQLite;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamoDbTest {
    private static String port;
    private static DynamoDBProxyServer server;

    @BeforeClass
    public static void beforeAll() throws Exception {
        configureEnvironment();
        port = String.valueOf(PortResolver.findOpenPort());
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

    private static void configureEnvironment() {
        setUpDummyAwsCredentials();
        /**
         * This is ugly! We have to tell SQLite4Java where the native sqlite libraries are located.
         * Our pom.xml file ensures they are copied from your local maven repository (~/.m2/repository)
         * to target/lib on compilation.
         * See this site for further details: https://code.google.com/p/sqlite4java/wiki/UsingWithMaven
         */
        SQLite.setLibraryPath("target/lib");
//        SQLite.setLibraryPath(System.getenv("HOME") + "/.m2/repository/com/almworks/sqlite4java/libsqlite4java-osx/1.0.392");
    }

    /**
     * The AWS SDK requires credentials to be set up, even if we only talk to DynamoDB Local.
     */
    private static void setUpDummyAwsCredentials() {
        System.setProperty("aws.accessKeyId", "dummyAccessKeyId");
        System.setProperty("aws.secretKey", "dummySecretKey");
    }
}
