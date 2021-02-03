package com.training.schedule.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.schedule.domain.schedule.session.SessionState;
import com.training.schedule.domain.schedule.session.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/*
*
* Add JsonProperty to temporarily handle with rest assured testing
*
* */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    @JsonProperty("schedule_id")
    private String scheduleId;
    private List<Vote> votes;
    private SessionState state;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
}
