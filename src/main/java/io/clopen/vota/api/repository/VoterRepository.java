package io.clopen.vota.api.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import io.clopen.vota.api.dto.VoterDto;
import java.util.Optional;

public interface VoterRepository extends ArangoRepository<VoterDto, String> {

  Optional<VoterDto> findByVoterId(String voterId);
}
