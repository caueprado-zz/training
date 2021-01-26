package com.training.schedule.domain.person.repository;

import com.training.schedule.domain.person.Person;

import java.util.Optional;

public interface PersonCustomRepository {

    Optional<Person> findByDocument(final String document);
}
