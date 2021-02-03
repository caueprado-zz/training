package com.training.schedule.controller.request;

import com.training.schedule.domain.schedule.session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {

    private String category;
    private String description;
    private String name;
    private Session session;
}
