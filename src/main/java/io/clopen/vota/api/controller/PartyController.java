package io.clopen.vota.api.controller;


import io.clopen.vota.api.model.Party;
import io.clopen.vota.api.model.ScoreBoard;
import io.clopen.vota.api.service.PartyService;
import io.clopen.vota.api.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartyController {

  private final PartyService partyService;
  private final VotingService votingService;

  @Autowired
  public PartyController(PartyService partyService, VotingService votingService) {
    this.partyService = partyService;
    this.votingService = votingService;
  }

  @GetMapping
  public List<Party> getAllParties() {
    return partyService.getAllParties();
  }

  @GetMapping("/scoreboard")
  public ScoreBoard getScoreBoard() {
    return votingService.getScoreboard();
  }
}

