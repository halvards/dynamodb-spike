package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;

public class Launch {
    private final DynamoDbLocal dynamoDbServer;
    private final ObjectMapper objectMapper;

    public static void main(String[] args) throws Exception {
        new Launch(new ObjectMapper()).run();
    }

    public Launch(ObjectMapper objectMapper) throws Exception {
        this.objectMapper = objectMapper;
        this.dynamoDbServer = new DynamoDbLocal();
        AwsCredentials.useDummy();
    }

    public void run() throws Exception {
        dynamoDbServer.run();

        get("/listtables", (request, response) -> {
            AmazonDynamoDBClient dynamoDbClient = new AmazonDynamoDBClient();
            dynamoDbClient.setEndpoint("http://localhost:" + dynamoDbServer.getPort());
            ListTablesResult listTablesResult = dynamoDbClient.listTables();
            return listTablesResult;
        }, objectMapper::writeValueAsString);
    }

}
