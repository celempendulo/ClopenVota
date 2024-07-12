package io.clopen.vota.api.service.impl;

import io.clopen.vota.api.dto.PartyDto;
import io.clopen.vota.api.model.Party;
import io.clopen.vota.api.repository.PartyRepository;
import io.clopen.vota.api.service.PartyService;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;

  @Autowired
  public PartyServiceImpl(PartyRepository partyRepository) {
    this.partyRepository = partyRepository;
    if (partyRepository.count() == 0) {
      saveStaticParties();
    }
  }


  @Override
  public List<Party> getAllParties() {
    var partiesIterator = partyRepository.findAll().spliterator();
    return StreamSupport.stream(partiesIterator, false)
        .map(it -> new Party(it.getPartyId(), it.getName(), it.getLogoUrl(), it.getAbbreviation()))
        .toList();
  }

  protected void saveStaticParties() {
    val parties = getStaticParties();
    val partDTOs = parties
        .stream()
        .map(it -> PartyDto.builder()
            .partyId(it.partyId())
            .name(it.name())
            .logoUrl(it.logoUrl())
            .abbreviation(it.abbreviation())
            .build())
        .toList();
    partyRepository.saveAll(partDTOs);
  }

  protected List<Party> getStaticParties() {
    return List.of(
        new Party(
            "ANC",
            "African National Congress",
            "https://upload.wikimedia.org/wikipedia/en/0/0d/African_National_Congress_logo.svg",
            "ANC"
        ),
        new Party(
            "DA",
            "Democratic Alliance",
            "https://upload.wikimedia.org/wikipedia/en/8/8f/Democratic_Alliance_%28SA%29_logo.svg",
            "DA"
        ),
        new Party(
            "EFF",
            "Economic Freedom Fighters",
            "https://effonline.org/wp-content/uploads/2023/12/cropped-economic-freedom-fighters-logo-B4832240E2-seeklogo.com_.png",
            "EFF"
        ),
        new Party(
            "IFP",
            "Inkatha Freedom Party",
            "https://upload.wikimedia.org/wikipedia/en/6/6e/Inkatha_Freedom_Party_logo.svg",
            "IFP"
        ),
        new Party(
            "VF_PLUS",
            "Freedom Front Plus",
            "https://upload.wikimedia.org/wikipedia/en/7/72/Freedom_Front_Plus.svg",
            "VF PLUS"
        ),
        new Party(
            "MK",
            "Umkhonto Wesizwe",
            "https://mkparty.org.za/wp-content/uploads/2023/12/uMkhonto-Wesizwe-logo.png",
            "MK"
        )
    );
  }

}
