package com.training.schedule.domain.person.repository;

import com.training.schedule.domain.person.Person;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class PersonCustomRepositoryImpl implements PersonCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Person> findByDocument(String document) {
        val query = createQuery(document);
        return ofNullable(mongoTemplate.findOne(query, Person.class));
    }

    private Query createQuery(final String document) {
        return new Query().addCriteria(createCriteria(document));
    }

    private Criteria createCriteria(final String document) {
        return Criteria.where("document").is(document);
    }
}
