package com.training.schedule.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.schedule.controller.mapper.PersonMapper;
import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.controller.response.PersonResponse;
import com.training.schedule.controller.validator.PersonCreationValidator;
import com.training.schedule.domain.exception.PersonAlreadyCreatedException;
import com.training.schedule.domain.person.Person;
import com.training.schedule.service.PersonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api("Api responsible to associate management")
@RestController
@RequestMapping("v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonCreationValidator personCreationValidator;

    @PostMapping
    @ApiOperation(value = "Create a new person, wich is capable to vote in sessions", tags = "Person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = PersonResponse.class),
            @ApiResponse(code = 403, message = "Obligatory field is missing"),
            @ApiResponse(code = 409, message = "Person already created", response = PersonAlreadyCreatedException.class)
    })
    public PersonResponse create(@RequestBody final PersonRequest personRequest) {
        personCreationValidator.validate(personRequest);
        return PersonMapper.toResponse(personService.create(personRequest));
    }

    @GetMapping
    @ApiOperation(value = "Find all persons", tags = "Person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = PersonResponse.class),
            @ApiResponse(code = 404, message = "Not found")})
    public List<Person> list() {
        return personService.find();
    }

}
