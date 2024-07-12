package io.clopen.vota.api.controller;


import io.clopen.vota.api.model.VoterStatus;
import io.clopen.vota.api.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VotingController {

  private final VotingService votingService;

  @Autowired
  public VotingController(VotingService votingService) {
    this.votingService = votingService;
  }

  @GetMapping("/status")
  public VoterStatus getVoterStatus(@CookieValue("voterId") String voterId) {
    return votingService.checkVoterStatus(voterId);
  }

  @PostMapping("/{partyId}")
  public void castVote(@CookieValue("voterId") String voterId, @PathVariable String partyId) {
    votingService.castVote(voterId, partyId);
  }

  @DeleteMapping("/")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void undoVote(@CookieValue("voterId") String voterId) {
    votingService.undoVote(voterId);
  }
}

