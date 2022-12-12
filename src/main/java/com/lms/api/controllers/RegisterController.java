package com.lms.api.controllers;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.constants.SystemName;
import com.lms.api.helpers.TransactionIdGenerator;
import com.lms.api.model.UrlRequest;
import com.lms.api.model.UrlResponse;
import com.lms.api.models.response.APIResponse;
import com.lms.api.service.RegisterService;
import com.lms.api.services.dbservices.TokenServiceImpl;
import com.lms.api.util.Constants;
import com.lms.api.utils.EncodeDecoder;
import com.lms.api.utils.LoggingHelper;
import io.micrometer.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constants.REGISTER_URL)
public class RegisterController {

    private final RegisterService registerService;

    private final TransactionIdGenerator transactionIdGenerator;


    private final TokenServiceImpl tokenService;

    private LoggingHelper loggingHelper = LoggingHelper.getInstance();


    /*****
     * This method would used to register the endpoint
     * TransactionId would be used to trace request in the logs in case there was an issue in the microservice.
     * So Transaction Id would always be returned the response header just incase the caller did not provide it.
     * **/

    @ApiOperation(value = "This operation is used to register url with Scoring system",
            notes = "This interface is used to register url",  response = APIResponse.class)
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> onboardurl(@Valid @RequestBody UrlRequest request,
                                      @RequestHeader(defaultValue = "", value = "transactionId", required = false) String transactionId
                                    ) {

        request.setUrl(Constants.TRANSACTION_URL); //Setting the transaction url to be registered at ScoringEngine Webservice
        request.setName(Constants.SERVICE_NAME); //setting the name of the middleware

        loggingHelper.requestObject(log, request);

        if(StringUtils.isEmpty(transactionId)){
            transactionId =transactionIdGenerator.generateTransactionId(SystemName.MIDDLEWARE.getName());
        }

        System.out.println("{}::: transactionId for the register endpoint "+ transactionId);

        log.info("{}::: transactionId for the register endpoint ", transactionId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(Constants.TRANSACTION_ID, transactionId);

        String finalTransactionId = transactionId;

        Mono<ResponseEntity> responseEntity = this.registerService.registerUrl(transactionId, request)
                .map(response->{

                    loggingHelper.responseObject(log, response);

                    if(response.getStatusCode().equals(NotificationStatusCode.OK.getCode())){

                        //Store inside the database below
                        //String bcryptPasshash = bCryptPasswordEncoder.encode(request.getPassword()); my prefered

                        String bcryptPasshash = EncodeDecoder.getHashUuid(request.getPassword());//wont use this in production.
                                String token = response.getData() != null ? ((UrlResponse)response.getData()).getToken() : "";

                        tokenService.storeCredentials(finalTransactionId, request.getUsername(), request.getPassword(), bcryptPasshash, token )
                                .map(item->{
                                    log.info("{}:::transactionId for the storing the registration details in db:: Response from module {}", finalTransactionId, item);

                                    return item;
                                }).onErrorResume(err->{

                                    log.error("{}:::transactionId for when the database error was logged storing the registration details in db {}", finalTransactionId, err);

                                    return Mono.just(false);
                                });

                         return ( ResponseEntity.status(HttpStatus.OK)
                                .headers(responseHeaders).body(response));
                    }
                    else{

                        //Mono.just
                        return ( ResponseEntity.status(response.getError().getStatus())
                                .headers(responseHeaders).body(response));

                    }
                });

        return responseEntity;
    }

}
