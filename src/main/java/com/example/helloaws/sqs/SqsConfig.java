package com.example.helloaws.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    private final String region;

    public SqsConfig(@Value("${aws.region}") String region) {
        this.region = region;
    }

    @Bean
    public SqsClient sqsClient() {
        // DefaultCredentialsProvider: .env의 AWS_ACCESS_KEY_ID/SECRET(또는 SESSION_TOKEN)이
        // 있으면 그것을, 없으면 EC2에 붙은 IAM 역할을 자동으로 사용한다.
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
