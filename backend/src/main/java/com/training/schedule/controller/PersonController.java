package com.training.schedule.controller;


import com.training.schedule.controller.mapper.PersonMapper;
import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.controller.response.PersonResponse;
import com.training.schedule.domain.exception.BadRequestException;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.person.Person;
import com.training.schedule.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static java.util.Objects.isNull;

@Api("Api responsible to create a new associate")
@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Person")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = PersonResponse.class),
        @ApiResponse(code = 403, message = "Obligatory field is missing"),
        @ApiResponse(code = 409, message = "Person already created", response = PersonAlreadyCreatedException.class)
    })
    public PersonResponse create(@RequestBody @Valid final PersonRequest personRequest) {
        if (isNull(personRequest.getDocument()) || isNull(personRequest.getName())) {
            throw new BadRequestException("Obligatory field is missing");
        }
        return PersonMapper.toResponse(personService.create(personRequest));
    }

    @GetMapping
    @ApiOperation(value = "Find all persons", tags = "Person")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = PersonResponse.class),
        @ApiResponse(code = 404, message = "Not found")})
    public List<Person> find() {
        return personService.find();
    }

}
