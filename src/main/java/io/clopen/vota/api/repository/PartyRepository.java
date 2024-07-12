package io.clopen.vota.api.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import io.clopen.vota.api.dto.PartyDto;
import java.util.Optional;

public interface PartyRepository extends ArangoRepository<PartyDto, String> {

  Optional<PartyDto> findByPartyId(String partyId);
}
