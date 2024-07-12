package io.clopen.vota.api.dto;

import com.arangodb.serde.jackson.Id;
import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document("vote")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class VoteDto {

  @Id
  private String id;
  private final String voterId;
  private final String partyId;
}
