package io.clopen.vota.vote;

import com.arangodb.serde.jackson.Id;

public record Vote(@Id String id, String voterId, String partyId) {

}
