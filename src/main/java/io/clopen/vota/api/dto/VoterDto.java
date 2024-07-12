package io.clopen.vota.api.dto;

import com.arangodb.serde.jackson.Id;
import com.arangodb.springframework.annotation.Document;
import lombok.Data;

@Document("voter")
@Data
public class VoterDto {

  @Id
  private String id;
  private String voterId;
  private String ipAddress;
  private String deviceType;
}
