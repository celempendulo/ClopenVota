package io.clopen.vota.vote;

import com.arangodb.springframework.repository.ArangoRepository;
import java.util.Optional;

public interface VoteRepository extends ArangoRepository<Vote, String> {

  int countByPartyId(String partyId);

  Optional<Vote> findByVoterId(String voterId);

  void deleteByVoterId(String voterId);

}
