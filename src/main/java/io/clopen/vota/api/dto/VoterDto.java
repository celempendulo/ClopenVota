package io.clopen.vota.api.dto;

import com.arangodb.serde.jackson.Id;
import com.arangodb.springframework.annotation.Document;
import lombok.Builder;
import lombok.Data;

@Document("voter")
@Data
@Builder
public class VoterDto {

  @Id
  private String id;
  private String voterId;
  private String ipAddress;
  private String deviceType;

}
