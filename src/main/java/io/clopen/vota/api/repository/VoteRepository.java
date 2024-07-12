package io.clopen.vota.api.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import io.clopen.vota.api.dto.VoteDto;
import java.util.Optional;

public interface VoteRepository extends ArangoRepository<VoteDto, String> {

  Optional<VoteDto> findByVoterId(String voterId);

  long countByPartyId(String partyId);

  void deleteByVoterId(String voterId);
}
