package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(considerNestedRepositories = true)
public class EmbeddedMongodbApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmbeddedMongodbApplication.class, args);
  }
}
