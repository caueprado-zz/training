package com.training.schedule.domain.schedule.repository;

import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.session.SessionState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String>, SessionCustomRepository,
    PagingAndSortingRepository<Schedule, String> {

    @Query("{ 'session.state' : ?0 }")
    List<Schedule> findSessionByState(final SessionState state);

    Page<Schedule> findAll(final Pageable page);
}
