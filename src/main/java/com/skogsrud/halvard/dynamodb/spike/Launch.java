package com.skogsrud.halvard.dynamodb.spike;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;

public class Launch {
    private final ObjectMapper objectMapper;
    private final DynamoDbLocal dynamoDbServer;
    private final AmazonDynamoDBClient dynamoDbClient;

    public static void main(String[] args) throws Exception {
        new Launch(new ObjectMapper()).run();
    }

    public Launch(ObjectMapper objectMapper) throws Exception {
        this.objectMapper = objectMapper;
        dynamoDbServer = new DynamoDbLocal();
        dynamoDbServer.start();
        dynamoDbClient = new AmazonDynamoDBClient(new BasicAWSCredentials("accessKey", "secretKey"));
        dynamoDbClient.setEndpoint("http://localhost:" + dynamoDbServer.getPort());
    }

    public void run() throws Exception {
        get("/listtables", (request, response) -> {
            return dynamoDbClient.listTables();
        }, objectMapper::writeValueAsString);
    }
}
