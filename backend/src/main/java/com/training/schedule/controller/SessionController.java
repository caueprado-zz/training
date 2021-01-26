package com.training.schedule.controller;


import com.training.schedule.controller.request.SessionResult;
import com.training.schedule.controller.request.SessionVoteRequest;
import com.training.schedule.controller.response.SessionResponse;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.exception.SessionAlreadyClosedException;
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
import org.springframework.web.bind.annotation.*;

import static com.training.schedule.controller.mapper.SessionMapper.toResponse;

@Api
@Slf4j
@RestController
@RequestMapping("session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final SessionVoteService sessionVoteService;
    private final SessionResultService sessionResultService;

    @PostMapping("{sessionId}/open/{minutes}")
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Session")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = SessionVoteRequest.class),
        @ApiResponse(code = 403, message = "Session cannot be open", response = SessionAlreadyClosedException.class),
        @ApiResponse(code = 404, message = "Session not found", response = SessionNotFoundException.class)
    })
    public SessionResponse open(@PathVariable final String sessionId, @PathVariable int minutes) {
        log.info("Opening session {} for {} minutes", sessionId, minutes);
        return toResponse(sessionService.openSession(sessionId, minutes));
    }

    @PutMapping("{sessionId}/close")
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Session")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = SessionVoteRequest.class),
        @ApiResponse(code = 403, message = "Session already open", response = SessionAlreadyClosedException.class)
    })
    public SessionResponse close(@PathVariable final String sessionId) {
        log.info("Closing session");
        return toResponse(sessionService.closeSession(sessionId));
    }

    @PostMapping("vote")
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Session")
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
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Session")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = SessionResult.class),
        @ApiResponse(code = 404, message = "Session not found")
    })
    public SessionResult getSessionResult(@PathVariable final String sessionId) {
        log.info("Getting session {} result", sessionId);
        return sessionResultService.getSessionResult(sessionId);
    }
}
