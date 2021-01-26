package com.training.schedule.service;

import com.training.schedule.TestBackground;
import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.person.Person;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.PersonValidationProducer;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest extends TestBackground {

    private static final String DOCUMENT = "123.111.442-31";
    private static final String NAME = "Tester";
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonValidationProducer personValidationProducer;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private DocumentClient documentClient;

    @Test(expected = PersonAlreadyCreatedException.class)
    public void givenAlreadyRegisteredPersonRequest_thenShouldNotRegister_AndThrowsAnException() {
        val expected = buildRequest();
        val person = Person.builder()
            .name(NAME)
            .document(DOCUMENT)
            .able(true)
            .build();
        personRepository.save(person);

        personService.create(expected);
    }

    private PersonRequest buildRequest() {
        return PersonRequest.builder()
            .name(NAME)
            .document(DOCUMENT)
            .build();
    }
}
