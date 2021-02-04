package com.training.schedule.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.cloud.contract.spec.internal.HttpStatus.OK;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.schedule.TestBackground;
import com.training.schedule.controller.endpoints.PersonEndpoints;
import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.controller.validator.PersonCreationValidator;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.PersonValidationProducer;

import io.restassured.RestAssured;
import lombok.val;

@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "management.port=8091", "management.context-path=/admin"})
public class PersonControllerTest extends TestBackground {

    private static final String FIELD_IS_MISSING = "Field is missing";
    private static final String MESSAGE = "message";

    @Autowired
    private PersonController personController;
    @MockBean
    private PersonValidationProducer personValidationProducer;
    @Autowired
    private PersonRepository personRepository;
    @MockBean
    private DocumentClient documentClient;
    @Autowired
    private PersonCreationValidator personCreationValidator;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = this.port;
    }

    @Test
    public void givenAValidPerson_WhenRequestCreation_ThenResponsesWithAPersonData() {
        val personRequest = PersonRequest.builder()
                .name("Teste")
                .document("123.213.333-45")
                .build();

        doNothing().when(personValidationProducer).validatePerson(any());

        PersonEndpoints.create(personRequest)
                .statusCode(OK);
    }

    @Test
    public void givenAInvalidPersonWithoutName_WhenRequestCreation_ThenResponsesWith() {
        val personRequest = PersonRequest.builder()
                .document("123")
                .build();
        doNothing().when(personValidationProducer).validatePerson(any());

        PersonEndpoints.create(personRequest)
                .statusCode(HttpStatus.BAD_REQUEST)
                .body(MESSAGE, equalTo(FIELD_IS_MISSING));
    }

    @Test
    public void givenAInvalidPersonWithoutDocument_WhenRequestCreation_ThenResponsesWith() {
        val personRequest = PersonRequest.builder()
                .name("Teste")
                .build();
        doNothing().when(personValidationProducer).validatePerson(any());

        PersonEndpoints.create(personRequest)
                .statusCode(HttpStatus.BAD_REQUEST)
                .body(MESSAGE, equalTo(FIELD_IS_MISSING));
    }

    @Test
    public void givenAValidPerson_WhenRequestToFind_ThenResponsesWithAPersonData() {
        val person = buildPersonWithState(true);

        personController.list();

        val result = PersonEndpoints.list().statusCode(OK)
                .extract()
                .jsonPath()
                .getList("$");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(person);
    }

    @After
    public void cleanup() {
        personRepository.deleteAll();
    }
}
