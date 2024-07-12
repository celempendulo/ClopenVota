package io.clopen.vota.api.dto;

import com.arangodb.serde.jackson.Id;
import com.arangodb.springframework.annotation.Document;
import lombok.Builder;
import lombok.Data;

@Document("vote")
@Data
@Builder
public class VoteDto {

  @Id
  private String id;
  private String voterId;
  private String partyId;
}
