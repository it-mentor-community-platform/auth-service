package com.itmentorcommunityplatform.authservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthEventProducer {

     private final KafkaTemplate<String, Object> kafkaTemplate;

     @Value("${kafka.topic.auth-user-created}")
     private String authUserCreatedTopic;

     public void sendUserCreated(long telegramUserId) {
         kafkaTemplate.send(authUserCreatedTopic, new UserCreatedEvent(telegramUserId));
     }
}
