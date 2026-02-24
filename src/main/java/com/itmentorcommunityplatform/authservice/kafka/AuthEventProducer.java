package com.itmentorcommunityplatform.authservice.kafka;

import com.itmentorcommunityplatform.authservice.kafka.exception.KafkaSendException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AuthEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.auth-user-created}")
    private String authUserCreatedTopic;

    @Value("${kafka.topic.auth-user-authenticated}")
    private String authUserAuthenticatedTopic;

    public void sendUserCreated(UserCreatedEvent userCreatedEvent) {
        try {
            kafkaTemplate.send(authUserCreatedTopic, userCreatedEvent).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaSendException(
                    "Thread interrupted while publishing to kafka creation event, telegramUserId: " + userCreatedEvent.telegramUserId(), e);
        } catch (ExecutionException | KafkaException | TimeoutException e) {
            throw new KafkaSendException(
                    "Failed to publish kafka creation event, for telegramUserId: " + userCreatedEvent.telegramUserId(), e);
        }
    }

    public void sendUserAuthenticated(UserAuthenticatedEvent userAuthenticatedEvent) {
        try {
            kafkaTemplate.send(authUserAuthenticatedTopic, userAuthenticatedEvent).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaSendException(
                    "Thread interrupted while publishing to kafka authentication event, telegramUserId: " + userAuthenticatedEvent.telegramUserId(), e);
        } catch (ExecutionException | KafkaException | TimeoutException e) {
            throw new KafkaSendException(
                    "Failed to publish kafka authentication event, for telegramUserId: " + userAuthenticatedEvent.telegramUserId(), e);
        }
    }
}
