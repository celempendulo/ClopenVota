package io.clopen.vota.api.controller;


import io.clopen.vota.api.model.Party;
import io.clopen.vota.api.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartyController {

  private final PartyService partyService;

  @Autowired
  public PartyController(PartyService partyService) {
    this.partyService = partyService;
  }

  @GetMapping
  public List<Party> getAllParties() {
    return partyService.getAllParties();
  }
}

