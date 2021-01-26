package com.training.schedule.infra.producer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class PersonValidationProducer {

    public static final String NEW_PERSON_QUEUE = "new.person.queue";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void validatePerson(final String personId) {
        log.info("Validating person with id {}", personId);
        rabbitTemplate.convertAndSend(NEW_PERSON_QUEUE, personId);
    }
}
