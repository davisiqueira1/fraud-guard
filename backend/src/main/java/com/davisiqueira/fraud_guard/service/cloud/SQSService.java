package com.davisiqueira.fraud_guard.service.cloud;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.Map;

@Service
public class SQSService {
    @Value("${aws.access_key}")
    private String accessKey;

    @Value("${aws.secret_key}")
    private String secretKey;

    @Value("${aws.region_name}")
    private String region;

    @Value("${aws.sqs_url}")
    private String queueUrl;

    private SqsAsyncClient client;

    @PostConstruct
    void createCredentials() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.client = SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public void sendMessageAsync(String messageBody, Map<String, MessageAttributeValue> attributes) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(attributes)
                .build();

        client.sendMessage(sendMessageRequest);
    }
}
