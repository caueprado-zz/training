package com.training.schedule.controller.response;

import com.training.schedule.domain.schedule.session.SessionState;
import com.training.schedule.domain.schedule.session.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private LocalDateTime dateTime;
    private String scheduleId;
    private List<Vote> votes;
    private SessionState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
