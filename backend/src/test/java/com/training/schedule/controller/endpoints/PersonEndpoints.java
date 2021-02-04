package com.training.schedule.controller.endpoints;

import static io.restassured.RestAssured.given;

import com.training.schedule.controller.request.PersonRequest;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public final class PersonEndpoints {

    private static final String PERSON_PATH = "v1/persons";

    public static ValidatableResponse create(final PersonRequest personRequest) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .with()
                .body(personRequest)
                .when()
                .post(PERSON_PATH)
                .peek()
                .then();
    }

    public static ValidatableResponse list() {
        return given()
                .when()
                .get(PERSON_PATH)
                .peek()
                .then();
    }
}
