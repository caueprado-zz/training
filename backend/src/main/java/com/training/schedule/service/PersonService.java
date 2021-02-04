package com.training.schedule.service;

import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.exception.PersonNotFoundException;
import com.training.schedule.domain.person.Person;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.PersonValidationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.training.schedule.infra.client.response.DocumentStatus.ABLE_TO_VOTE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private static final String DOCUMENT_ALREADY_EXISTS = "A Person with this document already exists";

    private final PersonRepository personRepository;
    private final PersonValidationProducer personValidationProducer;

    @Autowired
    private final DocumentClient documentClient;

    public Person create(final PersonRequest personRequest) {
        log.info("Creating new associate with name {} and document {}", personRequest.getName(), personRequest.getDocument());
        if (personRepository.findByDocument(personRequest.getDocument()).isPresent()) {
            String message = "Person with document $document, already exists.".replace("$document", personRequest.getDocument());
            log.error(message);
            throw new PersonAlreadyCreatedException(DOCUMENT_ALREADY_EXISTS);
        }

        val person = buildPerson(personRequest);
        val newPerson = personRepository.save(person);
        personValidationProducer.validatePerson(newPerson.getId());
        return newPerson;
    }

    private Person buildPerson(PersonRequest personRequest) {
        return Person.builder()
            .name(personRequest.getName())
            .document(personRequest.getDocument())
            .able(false)
            .build();
    }

    public void validatePersonDocument(final String personId) {
        val current = personRepository.findById(personId)
            .orElseThrow(PersonNotFoundException::new);

        val response = documentClient.request(current.getDocument());
        current.setAble(ABLE_TO_VOTE.equals(response.getStatus()));
        personRepository.save(current);
    }

    public Person findById(final String personId) {
        log.info("Getting person with id with {}", personId);
        return personRepository.findById(personId)
            .orElseThrow(PersonNotFoundException::new);
    }

    public List<Person> find() {
        log.info("Getting all persons");
        return personRepository.findAll();
    }
}
