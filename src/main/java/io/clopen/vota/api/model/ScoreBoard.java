package io.clopen.vota.api.model;

import java.util.List;

public record ScoreBoard(List<PartyScore> partyScores, int totalVotes) {

}

