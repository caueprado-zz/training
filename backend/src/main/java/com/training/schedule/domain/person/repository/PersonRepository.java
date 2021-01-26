package com.training.schedule.domain.person.repository;

import com.training.schedule.domain.person.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, String>, PersonCustomRepository {
}
