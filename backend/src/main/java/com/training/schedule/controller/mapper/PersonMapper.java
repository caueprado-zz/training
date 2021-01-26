package com.training.schedule.controller.mapper;

import com.training.schedule.controller.response.PersonResponse;
import com.training.schedule.domain.person.Person;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class PersonMapper {

    public PersonResponse toResponse(final Person person) {
        return PersonResponse.builder()
            .name(person.getName())
            .document(person.getDocument())
            .able(person.isAble())
            .build();
    }
}
