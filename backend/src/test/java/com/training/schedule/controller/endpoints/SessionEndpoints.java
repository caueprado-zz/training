package com.training.schedule.controller.endpoints;

import static io.restassured.RestAssured.given;

import com.training.schedule.controller.request.SessionVoteRequest;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public final class SessionEndpoints {

    private static final String SESSION_PATH = "v1/sessions";

    public static ValidatableResponse open(final String sessionId, final int minutes) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .with()
                .pathParam("sessionId", sessionId)
                .pathParam("minutes", minutes)
                .when()
                .post(SESSION_PATH + "/{sessionId}/opening/{minutes}")
                .peek()
                .then();
    }

    public static ValidatableResponse close(final String sessionId) {
        return given()
                .with()
                .pathParam("sessionId", sessionId)
                .when()
                .put(SESSION_PATH + "/{sessionId}/closing")
                .peek()
                .then();
    }

    public static ValidatableResponse vote(final SessionVoteRequest voteRequest, final String sessionId) {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .with()
                .pathParam("sessionId", sessionId)
                .and()
                .body(voteRequest)
                .when()
                .post(SESSION_PATH + "/{sessionId}/vote")
                .peek()
                .then();
    }

    public static ValidatableResponse result(final String sessionId) {
        return given()
                .with()
                .pathParam("sessionId", sessionId)
                .when()
                .get(SESSION_PATH + "/{sessionId}/result")
                .peek()
                .then();
    }
}
