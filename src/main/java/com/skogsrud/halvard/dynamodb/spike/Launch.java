package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

import static spark.Spark.get;

public class Launch {
    private final DynamoDbLocal dynamoDbLocal;

    public static void main(String[] args) throws Exception {
        new Launch().run();
    }

    public Launch() throws Exception {
        this.dynamoDbLocal = new DynamoDbLocal();
        setUpDummyAwsCredentials();
    }

    public void run() throws Exception {
        dynamoDbLocal.run();

        get("/listtables", (request, response) -> {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient();
            client.setEndpoint("http://localhost:" + dynamoDbLocal.getPort());
            ListTablesResult listTablesResult = client.listTables();
            return listTablesResult.toString();
        });
    }

    private static void setUpDummyAwsCredentials() {
        System.setProperty("aws.accessKeyId", "dummyAccessKeyId");
        System.setProperty("aws.secretKey", "dummySecretKey");
    }
}
