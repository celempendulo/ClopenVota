package io.clopen.vota.api.service.impl;

import io.clopen.vota.api.dto.VoteDto;
import io.clopen.vota.api.dto.VoterDto;
import io.clopen.vota.api.model.Voter;
import io.clopen.vota.api.repository.VoterRepository;
import io.clopen.vota.api.service.VoterService;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoterServiceImpl implements VoterService {

  private final VoterRepository voterRepository;

  @Autowired
  public VoterServiceImpl(VoterRepository voterRepository) {
    this.voterRepository = voterRepository;
  }

  @Override
  public Optional<Voter> findByVoterId(String voterId) {
    return voterRepository.findByVoterId(voterId).map(it -> new Voter(it.getVoterId(), it.getIpAddress(), it.getDeviceType()));
  }

  @Override
  public void add(Voter voter) {
    val voterDtoOptional = voterRepository.findByVoterId(voter.voterId());
    if (voterDtoOptional.isPresent()) {
      throw new RuntimeException("Voter ID is already taken");
    }
    val voterDto = VoterDto.builder()
        .voterId(voter.voterId())
        .ipAddress(voter.ipAddress())
        .deviceType(voter.deviceType())
        .build();
    voterRepository.save(voterDto);
  }
}
