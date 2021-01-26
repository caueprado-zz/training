package com.training.schedule.domain.schedule.session;

import com.training.schedule.controller.request.VoteOption;
import com.training.schedule.domain.person.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    private Person person;
    private VoteOption vote;
}
