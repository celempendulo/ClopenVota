package io.clopen.vota.api.service.impl;

import io.clopen.vota.api.dto.VoteDto;
import io.clopen.vota.api.model.Party;
import io.clopen.vota.api.model.PartyScore;
import io.clopen.vota.api.model.ScoreBoard;
import io.clopen.vota.api.model.VoterStatus;
import io.clopen.vota.api.repository.PartyRepository;
import io.clopen.vota.api.repository.VoteRepository;
import io.clopen.vota.api.repository.VoterRepository;
import io.clopen.vota.api.service.VotingService;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoterServiceImpl implements VotingService {

  private final VoterRepository voterRepository;
  private final PartyRepository partyRepository;
  private final VoteRepository voteRepository;

  @Autowired
  public VoterServiceImpl(VoterRepository voterRepository, VoteRepository voteRepository, PartyRepository partyRepository) {
    this.voterRepository = voterRepository;
    this.voteRepository = voteRepository;
    this.partyRepository = partyRepository;
  }

  @Override
  public VoterStatus checkVoterStatus(String voterId) {
    return voteRepository.findByVoterId(voterId)
        .map(it -> new VoterStatus(it.getVoterId(), true))
        .orElse(new VoterStatus(voterId, false));
  }

  @Override
  public void castVote(String voterId, String partyId) {
    val voterStatus = checkVoterStatus(voterId);
    if(voterStatus.voted()) {
      throw new RuntimeException("User already voter");
    }

    val partyOptional = partyRepository.findByPartyId(partyId);
    if(partyOptional.isEmpty()) {
      throw new RuntimeException("Party is not recognized");
    }
    voteRepository.save(new VoteDto(voterId, partyOptional.get().getPartyId()));
  }

  @Override
  public void undoVote(String voterId) {
    val voterStatus = checkVoterStatus(voterId);
    if(!voterStatus.voted()) {
      throw new RuntimeException("User did not vote");
    }
    voteRepository.deleteByVoterId(voterId);
  }

  @Override
  public ScoreBoard getScoreboard() {
    val parties = partyRepository.findAll();
    val votes = voteRepository.count();

    val partyScores = StreamSupport.stream(parties.spliterator(), false).map( it -> {
      val party = new Party(it.getPartyId(), it.getName(), it.getLogoUrl(), it.getAbbreviation());
      val partyVotes = voteRepository.countByPartyId(it.getPartyId());
      return new PartyScore(party, partyVotes, votes == 0 ? 0.0 : (double)partyVotes / votes);
    });

    val sortedParties = partyScores.sorted((first, second) -> {
      if (first.votes() != second.votes())
        return (int) (second.votes() - first.votes());
      else
        return first.party().name().compareTo(second.party().name());
    }).toList();

    return new ScoreBoard(sortedParties, (int)votes);
  }
}
