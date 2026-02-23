package com.itmentorcommunityplatform.authservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Value("${kafka.topic.auth-user-created}")
    private String authUserCreatedTopic;

    @Value("${kafka.topic.auth-user-authenticated}")
    private String authUserAuthenticatedTopic;

    @Bean
    public NewTopic authUserCreatedTopic() {
        return TopicBuilder.name(authUserCreatedTopic)
                .build();
    }

    @Bean
    public NewTopic authUserAuthenticatedTopic() {
        return TopicBuilder.name(authUserAuthenticatedTopic)
                .build();
    }
}
