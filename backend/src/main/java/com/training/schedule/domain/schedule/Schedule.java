package com.training.schedule.domain.schedule;

import com.training.schedule.domain.schedule.session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Schedule {

    @Indexed
    private String id;
    private String category;
    private String description;
    private String name;
    private LocalDateTime date;
    private Session session;

}
