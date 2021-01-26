package com.training.schedule.controller.request;

import com.training.schedule.domain.schedule.session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {

    private String category;
    private String description;
    private String name;
    private LocalDateTime date;
    private Session session;
}