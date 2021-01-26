package com.training.schedule.infra.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionClosedProducer {

    public static final String SESSION_CLOSED_QUEUE = "session.closed.queue";
    private final RabbitTemplate rabbitTemplate;

    public void close(final String sessionId) {
        log.info("Session {}, closed!", sessionId);
        rabbitTemplate.convertAndSend(SESSION_CLOSED_QUEUE, sessionId);
    }
}
