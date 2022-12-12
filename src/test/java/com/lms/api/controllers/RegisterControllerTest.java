package com.lms.api.controllers;

import com.lms.api.constants.NotificationStatusCode;
import org.hamcrest.Matchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.api.ReactiveappApplication;
import com.lms.api.model.UrlRequest;
import com.lms.api.test.TestNumbers;
import com.lms.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveappApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class RegisterControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Test
    void LoanRequestShouldBeSuccessful() throws Exception {

        UrlRequest request  = new UrlRequest();
        request.setName(Constants.SERVICE_NAME);
        request.setUrl(Constants.TRANSACTION_URL);
        request.setUsername("username");
        request.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post(Constants.REGISTER_URL+"/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apiResponse.statusCode", Matchers.is(NotificationStatusCode.OK.getCode())))
                .andExpect(jsonPath("$.apiResponse.data.name", Matchers.is(request.getName())))
                .andExpect(jsonPath("$.apiResponse.data.username", Matchers.is(request.getUsername())))
                .andExpect(jsonPath("$.apiResponse.data.password", Matchers.is(request.getPassword())))
                .andExpect(jsonPath("$.apiResponse.data.token", Matchers.anything()));
                //.andExpect(jsonPath("$.respBody.amount", Matchers.is("2000")))
               // .andExpect(jsonPath("$.respBody.currencyCode", Matchers.is("NAIRA")));

        /*
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.respCode", Matchers.is("00")))
                .andExpect(jsonPath("$.respDescription", Matchers.is("success")))
                .andExpect(jsonPath("$.respBody.date", Matchers.is("2021-12-20")))
                .andExpect(jsonPath("$.respBody.amount", Matchers.is("2000")))
                .andExpect(jsonPath("$.respBody.currencyCode", Matchers.is("NAIRA")))*/

    }


}
