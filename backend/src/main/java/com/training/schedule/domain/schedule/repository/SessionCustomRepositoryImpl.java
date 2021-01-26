package com.training.schedule.domain.schedule.repository;

import com.training.schedule.domain.schedule.Schedule;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.training.schedule.domain.schedule.session.SessionState.OPEN;

@Repository
public class SessionCustomRepositoryImpl implements SessionCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Schedule> findOpenSession(final LocalDateTime currentTime) {
        val query = createQuery(currentTime);
//        PageableExecutionUtils.getPage();
        return mongoTemplate.find(query, Schedule.class);
    }

    private Query createQuery(final LocalDateTime currentTime) {
        val query = new Query();
        query.addCriteria(createCriteria(currentTime));
        return query;
    }

    private Criteria createCriteria(final LocalDateTime currentTime) {
        return Criteria.where("session.state").is(OPEN)
            .and("session.endTime").lt(currentTime);
    }

}
