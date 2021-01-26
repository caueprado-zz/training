package com.training.schedule.domain.schedule.session;

import com.training.schedule.controller.request.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.training.schedule.domain.schedule.session.SessionState.OPEN;
import static java.util.Objects.isNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("session")
public class Session {

    private List<Vote> votes;
    private SessionState state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean isOpen() {
        return OPEN.equals(this.state);
    }

    public boolean voted(final String personId) {
        if (isNull(votes)) {
            return false;
        }

        return votes.stream()
            .map(Vote::getPerson)
            .anyMatch(person -> person.getId().equals(personId));
    }

    public void addVote(final Vote vote) {
        nullOrEmpty();
        this.votes.add(vote);
    }

    private void nullOrEmpty() {
        if (isNull(this.votes)) this.votes = new ArrayList<>();
    }

    public long getVotesOf(final VoteOption voteOption) {
        nullOrEmpty();
        return this.votes.stream()
            .filter(vote -> voteOption.equals(vote.getVote()))
            .count();
    }

    public int getVoteCount() {
        if (isNull(votes)) {
            return 0;
        }
        return this.votes.size();
    }
}
