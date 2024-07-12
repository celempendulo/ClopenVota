package io.clopen.vota;

import io.clopen.vota.db.Initializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClopenVotaApplication {

  public static void main(String[] args) {
    var context = SpringApplication.run(ClopenVotaApplication.class, args);
    var initializer = context.getBean(Initializer.class);
    initializer.init();

  }

}
