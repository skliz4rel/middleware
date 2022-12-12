package com.lms.api.utility;

import com.lms.api.ReactiveappApplication;
import com.lms.api.http.WebclientUtility;
import com.lms.api.model.UrlRequest;
import com.lms.api.model.UrlResponse;
import com.lms.api.properties.AppProperties;
import com.lms.api.test.TestNumbers;
import com.lms.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.ssl.SSLException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveappApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class WebclientUtilityTest {

    @Autowired
    private WebclientUtility webclientUtility;

    @Autowired
    private AppProperties appProperties;

    private String headerToken = "56c7e180-f264-4095-b3b6-5e169c38a7a9";

    private String tokenForGettingscore = "ee05eace-2c62-4ff2-a80a-d7c9972cf52c";

    @Test
    public void postTest() throws SSLException {

        try {
            /*UrlRequest request = new UrlRequest();
            request.setName(Constants.SERVICE_NAME);
            request.setUrl(Constants.TRANSACTION_URL);
            request.setUsername("username");
            request.setPassword("password");*/

            String fulluri = appProperties.getScoringEngineBaseUri()+appProperties.getInitiateQueryScoreEndpoint()+ TestNumbers.CustomerID_1;


            Map<String, String> headers = new HashMap<>();
            headers.put("client-token",headerToken);

            webclientUtility.get(fulluri,
                            headers,
                    UrlResponse.class
                    ).cast(UrlResponse.class)
                    .flatMap(item -> {

                        System.out.println("token recieved " + item);


                        return item;
                    }).onErrorResume(item->{

                        log.error("Error returned {}",item);

                        return item;
                    });
        }
        catch (Exception e){
            log.error("There is an error  {}",e);
        }
    }



    @Test
    public void getScoring() throws SSLException {

        try {

            String fulluri = appProperties.getScoringEngineBaseUri()+appProperties.getQueryScoreEndpoint();

            log.info("This is the endpoint been called {}", fulluri);

            Map<String, String> headers = new HashMap<>();
            headers.put("client-token",headerToken);

            String result = (String)  webclientUtility.get(fulluri + tokenForGettingscore,
                            headers, String.class
                    ).cast(String.class)
                    .flatMap(item -> {

                        System.out.println("token recieved " + item);

                        log.info("token recieved " + item);

                        return item;
                    }).onErrorResume(item->{

                        log.error("error returned {}", item);

                        return item;
                    }).block();

          log.info("result is  "+result);
        }
        catch (Exception e){
            log.error("There is an error  {}",e);
        }
    }


}
