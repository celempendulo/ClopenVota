package io.clopen.vota.api.service;

import io.clopen.vota.api.model.Voter;
import java.util.Optional;

public interface VoterService {

  Optional<Voter> findByVoterId(String voterId);


  void add(Voter voter);
}
