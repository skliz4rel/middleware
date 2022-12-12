package com.lms.api.service;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.http.HttpErrorHelper;
import com.lms.api.model.UrlRequest;
import com.lms.api.model.UrlResponse;
import com.lms.api.http.WebclientUtility;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import com.lms.api.properties.AppProperties;
import io.swagger.annotations.ApiResponse;
import io.swagger.models.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/*******
 *  RegisterService this is going to be used to initiate the modules to register the endpoint that the ScoringEngine
 *  would use to communciiate with the middleware service.
 * *****/

@Slf4j
@Service
public class RegisterService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebclientUtility webclientUtility;

    @Autowired
    private HttpErrorHelper httpErrorHelper;


    public Mono<APIResponse> registerUrl (String transactionId, UrlRequest request){

        String fulluri = appProperties.getScoringEngineBaseUri()+appProperties.getRegisterEndpoint();

        try {
           return this.webclientUtility.post(fulluri,  request,null,UrlResponse.class)
                    .cast(UrlResponse.class)
                    .map(response->{

                        log.info("{} transactionId ::: feedback of registering url {}", transactionId, response);

                        APIResponse apiResponse = new APIResponse();
                        apiResponse.setStatusCode(NotificationStatusCode.OK.getCode());
                        apiResponse.setStatusMessage(NotificationStatusCode.OK.getDescription());

                        apiResponse.setData(response);

                        return apiResponse;
                    }).onErrorResume((ex)->{
                        log.error("{}:::transactionId error while processing register endpoint request {}", transactionId, ex);

                        WebClientResponseException webExcep = null;

                        if(ex instanceof  WebClientResponseException){
                            webExcep = (WebClientResponseException) ex;
                        }

                        return httpErrorHelper.returnErrResponse(transactionId, webExcep,
                                NotificationStatusCode.HTTP_CONNECTION_ERROR.getCode(),
                                NotificationStatusCode.HTTP_CONNECTION_ERROR.getDescription(),
                                NotificationStatusCode.HTTP_CONNECTION_ERROR.getHttpStatus() );
                    });

        }
        catch (WebClientResponseException | IOException ex){

            return httpErrorHelper.returnErrResponse(transactionId, ex,
                    NotificationStatusCode.HTTP_CONNECTION_ERROR.getCode(),
                    NotificationStatusCode.HTTP_CONNECTION_ERROR.getDescription(),
                    NotificationStatusCode.HTTP_CONNECTION_ERROR.getHttpStatus() );
        }
    }


}
