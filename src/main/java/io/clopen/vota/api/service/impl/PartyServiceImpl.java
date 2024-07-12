package io.clopen.vota.api.service.impl;

import io.clopen.vota.api.model.Party;
import io.clopen.vota.api.repository.PartyRepository;
import io.clopen.vota.api.service.PartyService;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;

  @Autowired
  public PartyServiceImpl(PartyRepository partyRepository) {
    this.partyRepository = partyRepository;
  }


  @Override
  public List<Party> getAllParties() {
    var partiesIterator = partyRepository.findAll().spliterator();
    return StreamSupport.stream(partiesIterator, false)
        .map(it -> new Party(it.getPartyId(), it.getName(), it.getLogoUrl(), it.getAbbreviation()))
        .toList();
  }

}
