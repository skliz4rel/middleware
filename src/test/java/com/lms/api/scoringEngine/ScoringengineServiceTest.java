package com.lms.api.scoringEngine;

import com.lms.api.ReactiveappApplication;
import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.models.response.APIResponse;
import com.lms.api.repositorys.TokenRepository;
import com.lms.api.services.scoringapicalls.ScoringengineService;
import com.lms.api.test.TestNumbers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveappApplication.class)
@ExtendWith(SpringExtension.class)
public class ScoringengineServiceTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ScoringengineService scoringengineService;

    private  String traceTransactionId = UUID.randomUUID().toString();

    @Test
    public void getKycscore(){

        var token = this.tokenRepository.findAll().collectList().block();

        log.info("token objects pull from the db {}", token);

        if(token != null && token.size() >0){

           APIResponse apiResponse =  this.scoringengineService.initiateQueryScore_returnKycScore(traceTransactionId, token.get(0).getScoringEngineToken(), TestNumbers.CustomerID_2).block();

           assertThat(apiResponse).isNotNull();
           assertThat(apiResponse.getStatusCode()).isEqualTo(NotificationStatusCode.OK.getCode());
           assertThat(apiResponse.getData()).isNotNull();
        }


    }


}
