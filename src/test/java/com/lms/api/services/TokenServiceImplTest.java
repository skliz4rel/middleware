package com.lms.api.services;

import com.lms.api.ReactiveappApplication;
import com.lms.api.services.dbservices.TokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveappApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class TokenServiceImplTest {

    private String traceTransactionId= UUID.randomUUID().toString();

    @Autowired
    private TokenServiceImpl tokenService;

    @Test
    public void testTokenmodules(){

       boolean check =  this.tokenService.storeCredentials(traceTransactionId,"username", "password","haspassword","token").block();

        assertThat(check).isTrue();
    }

}
