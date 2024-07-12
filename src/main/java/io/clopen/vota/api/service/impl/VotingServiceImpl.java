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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingServiceImpl implements VotingService {

  private final VoterRepository voterRepository;
  private final PartyRepository partyRepository;
  private final VoteRepository voteRepository;

  @Autowired
  public VotingServiceImpl(VoterRepository voterRepository, VoteRepository voteRepository, PartyRepository partyRepository) {
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
      throw new RuntimeException(String.format("Party [partyId=%s] is not recognized", partyId));
    }
    val voteDto = VoteDto.builder().voterId(voterId).partyId(partyId).build();
    voteRepository.save(voteDto);
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

    if (votes == 0) {
      val sortedParties = StreamSupport.stream(parties.spliterator(), false)
          .map( it -> new Party(it.getPartyId(), it.getName(), it.getLogoUrl(), it.getAbbreviation()))
          .map(it -> new PartyScore(it, 0, 0.0d))
          .sorted(getPartyScoreComparator())
          .toList();
      return new ScoreBoard(sortedParties, (int)votes);
    }

    val partyScores = StreamSupport.stream(parties.spliterator(), false).map( it -> {
      val party = new Party(it.getPartyId(), it.getName(), it.getLogoUrl(), it.getAbbreviation());
      val partyVotes = voteRepository.countByPartyId(it.getPartyId());
      val percentage = new BigDecimal(100.0 * partyVotes / votes)
          .setScale(1, RoundingMode.HALF_EVEN)
          .doubleValue();
      return new PartyScore(party, partyVotes, percentage);
    });
    val sortedParties = partyScores.sorted(getPartyScoreComparator()).toList();
    return new ScoreBoard(sortedParties, (int)votes);
  }


  private Comparator<PartyScore> getPartyScoreComparator() {
    return (first, second) -> {
      if (first.votes() != second.votes())
        return (int) (second.votes() - first.votes());
      else
        return first.party().name().compareTo(second.party().name());      };
  }
}
