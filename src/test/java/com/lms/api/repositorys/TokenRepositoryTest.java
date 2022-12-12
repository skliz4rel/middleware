package com.lms.api.repositorys;

import com.lms.api.db.Token;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
@Slf4j
@DataR2dbcTest
@ExtendWith(SpringExtension.class)
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    private Token tokens;


      Token initialize(){
        tokens = new Token();
       // tokens.setId(UUID.randomUUID().toString());
        tokens.setUsername("username");
        tokens.setBasicAuthstr("basickAuthotoken####");
        tokens.setHashPassword("23423412412342134");
        tokens.setScoringEngineToken("13123-132-123123-12312313");
        //tokens.setId(1);

        return tokens;
    }


    @Test
    public void TestSavetoken(){

          try {
              tokens = initialize();

              tokenRepository.save(tokens).map(item -> {

                  log.info("saving response {}", item);

                  return item;
              }).onErrorResume(err -> {
                  log.error("error thrown when saving token {}", err);

                  return Mono.just(tokens);
              }).block();

              Mono<Token> tokenDataMono = this.tokenRepository.findByUsername("username");

              log.info("token returned {}" + tokenDataMono);

              var token = tokenDataMono.block();

              assertThat(token).isNotNull();

              tokenDataMono = this.tokenRepository.findByUsernameAndHashPassword(tokens.getUsername(), tokens.getHashPassword());

              token = tokenDataMono.block();

              assertThat(token).isNotNull();


              this.tokenRepository.deleteAll().block();
          }
          catch (Exception e){
              log.error("error whlie testing the token repository {}",e);
          }
     }


   // @Test
    public void countToken(){

        /**
         * final long count = repo.count().block();
         * assertEquals(10871L, count);
         * **/

//        StepVerifier
//                .create(tokenRepository.count())
//                .expectNext(1l)
//                .expectComplete()
//                .verify();

       /* StepVerifier
                .create(tokenRepository.findAll().collectList())
                .expectNextMatches(list -> list.list() == 10871)
                .expectComplete()
                .verify();*/


        /*

        StepVerifier
    .create(repo.findAllByCorporationId(1000120).collectList())
    .expectNextMatches(list -> list.size() == 144)
    .expectComplete()
    .verify();
StepVerifier
    .create(repo.findAllByLocationId(60008368).collectList())
    .expectNextMatches(list -> list.size() == 18)
    .expectComplete()
    .verify();
         */
    }
}
