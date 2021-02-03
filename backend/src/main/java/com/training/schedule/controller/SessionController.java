package com.training.schedule.controller;


import static com.training.schedule.controller.mapper.SessionMapper.toResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.schedule.controller.request.SessionResult;
import com.training.schedule.controller.request.SessionVoteRequest;
import com.training.schedule.controller.response.SessionResponse;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.exception.SessionClosedException;
import com.training.schedule.domain.exception.SessionNotFoundException;
import com.training.schedule.service.SessionResultService;
import com.training.schedule.service.SessionService;
import com.training.schedule.service.SessionVoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api("Sessão")
@Slf4j
@RestController
@RequestMapping("v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final SessionVoteService sessionVoteService;
    private final SessionResultService sessionResultService;

    @PostMapping("{sessionId}/opening/{minutes}")
    @ApiOperation(value = "Open a session of a created Schedule", tags = "Session")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = SessionVoteRequest.class),
            @ApiResponse(code = 403, message = "Session cannot be open", response = SessionClosedException.class),
            @ApiResponse(code = 404, message = "Session not found", response = SessionNotFoundException.class)
    })
    public SessionResponse open(@PathVariable final String sessionId, @PathVariable int minutes) {
        log.info("Opening session {} for {} minutes", sessionId, minutes);
        return toResponse(sessionService.openSession(sessionId, minutes));
    }

    @PutMapping("{sessionId}/closing")
    @ApiOperation(value = "Close a open session", tags = "Session")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = SessionVoteRequest.class),
            @ApiResponse(code = 403, message = "Session already open", response = SessionClosedException.class)
    })
    public SessionResponse close(@PathVariable final String sessionId) {
        log.info("Closing session");
        return toResponse(sessionService.closeSession(sessionId));
    }

    @PostMapping("vote")
    @ApiOperation(value = "Let a person to vote in a open session", tags = "Session")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = SessionVoteRequest.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Person already voted", response = PersonAlreadyCreatedException.class)
    })
    public SessionVoteRequest vote(@RequestBody final SessionVoteRequest voteRequest) {
        log.info("Receive new vote from person with Id: {}", voteRequest.getPersonId());
        sessionVoteService.vote(voteRequest);
        return voteRequest;
    }

    @GetMapping("{sessionId}/result")
    @ApiOperation(value = "Return result of a session", tags = "Session")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = SessionResult.class),
            @ApiResponse(code = 404, message = "Schedule not found")
    })
    public SessionResult get(@PathVariable final String sessionId) {
        log.info("Getting session {} result", sessionId);
        return sessionResultService.getSessionResult(sessionId);
    }
}
