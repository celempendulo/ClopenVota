package io.clopen.vota.api.service;


import io.clopen.vota.api.model.ScoreBoard;
import io.clopen.vota.api.model.VoterStatus;

public interface VotingService {

  void castVote(String voterId, String partyId);

  void undoVote(String voterId);

  VoterStatus checkVoterStatus(String voterId);

  ScoreBoard getScoreboard();
}

