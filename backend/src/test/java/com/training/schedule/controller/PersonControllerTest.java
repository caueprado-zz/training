package com.training.schedule.controller;

import com.training.schedule.TestBackground;
import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.domain.exception.BadRequestException;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.PersonValidationProducer;
import lombok.val;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTest extends TestBackground {

    @Autowired
    private PersonController personController;

    @MockBean
    private PersonValidationProducer personValidationProducer;

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private DocumentClient documentClient;

    @Test
    public void givenAValidPerson_WhenRequestCreation_ThenResponsesWithAPersonData() {
        val personRequest = PersonRequest.builder()
            .name("Teste")
            .document("123")
            .build();

        doNothing().when(personValidationProducer).validatePerson(any());

        val result = personController.create(personRequest);

        assertThat(result.getName()).isEqualTo(personRequest.getName());
        assertThat(result.getDocument()).isEqualTo(personRequest.getDocument());
    }

    @Test(expected = BadRequestException.class)
    public void givenAInvalidPersonWithoutName_WhenRequestCreation_ThenResponsesWith() {
        val personRequest = PersonRequest.builder()
            .document("123")
            .build();
        doNothing().when(personValidationProducer).validatePerson(any());

        val result = personController.create(personRequest);

        assertThat(result.getName()).isEqualTo(personRequest.getName());
        assertThat(result.getDocument()).isEqualTo(personRequest.getDocument());
    }

    @Test(expected = BadRequestException.class)
    public void givenAInvalidPersonWithoutDocument_WhenRequestCreation_ThenResponsesWith() {
        val personRequest = PersonRequest.builder()
            .name("Teste")
            .build();
        doNothing().when(personValidationProducer).validatePerson(any());

        personController.create(personRequest);
    }

    @Test
    public void givenAValidPerson_WhenRequestToFind_ThenResponsesWithAPersonData() {
        val person = buildPersonWithState(true);

        val result = personController.list();

        assertThat(result.size()).isEqualTo(1);
        result.forEach(
            person1 -> {
                assertThat(person1.getName()).isEqualTo(person.getName());
                assertThat(person1.getDocument()).isEqualTo(person.getDocument());
            }
        );
    }

    @After
    public void cleanup() {
        personRepository.deleteAll();;
    }
}
