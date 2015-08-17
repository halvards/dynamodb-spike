package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class DynamoDbTest {
    private static String port;
    private static DynamoDBProxyServer server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        configureEnvironment();
        port = String.valueOf(PortResolver.findOpenPort());
        server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", port});
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    public void tableListShouldBeEmptyOnLaunch() throws Exception {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials("accessKey", "secretKey"));
        client.setEndpoint("http://localhost:" + port);
        ListTablesResult listTablesResult = client.listTables();
        assertThat(listTablesResult.getTableNames(), empty());
    }

    private static void configureEnvironment() {
        /**
         * This is ugly! We have to tell SQLite4Java where the native sqlite libraries are located.
         * Our pom.xml file ensures they are copied from your local maven repository (~/.m2/repository)
         * to target/lib on compilation.
         * See this site for further details: https://code.google.com/p/sqlite4java/wiki/UsingWithMaven
         */
        System.setProperty("sqlite4java.library.path", "target/lib");
    }
}
