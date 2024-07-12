package io.clopen.vota.api.dto;

import com.arangodb.serde.jackson.Id;
import com.arangodb.springframework.annotation.Document;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document("party")
@Data
@Builder
public class PartyDto {

  @Id
  private String id;
  private String partyId;
  private String name;
  private String logoUrl;
  private String abbreviation;

}
