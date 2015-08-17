package com.skogsrud.halvard.dynamodb.spike;

public class AwsCredentials {
    /**
     * The AWS SDK requires credentials to be set up, even if we only talk to DynamoDB Local.
     */
    public static void useDummy() {
        System.setProperty("aws.accessKeyId", "dummyAccessKeyId");
        System.setProperty("aws.secretKey", "dummySecretKey");
    }
}
