package com.training.schedule.infra.listener;

import com.training.schedule.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonListener {

    public static final String NEW_PERSON_QUEUE = "new.person.queue";
    private final PersonService personService;

    @RabbitListener(queues = NEW_PERSON_QUEUE)
    public void onMessage(final String personId) {
        log.info("Person with id: {}, is to be verified", personId);
        if(personId.isEmpty()) {
            log.error("Person id is blank.");
        } else {
//            try {
                personService.validatePersonDocument(personId);
//            } catch (Exception e) {
//                throw new Exception("Not Recoverable message");
//            }
        }
    }
}
