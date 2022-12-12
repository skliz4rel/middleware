package com.lms.api.controllers;

import com.lms.api.constants.SystemName;
import com.lms.api.helpers.TransactionIdGenerator;
import com.lms.api.model.TransactionDTO;
import com.lms.api.model.UrlResponse;
import com.lms.api.models.response.APIResponse;
import com.lms.api.service.BasicAuthorizationUtil;
import com.lms.api.services.soapservices.apicall.TransactionService;
import com.lms.api.services.soapservices.transaction.TransactionData;
import com.lms.api.util.Constants;
import com.lms.api.util.MappingUtility;
import com.lms.api.utils.LoggingHelper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.lms.api.services.soapservices.transaction.TransactionsResponse;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constants.TRANSACTION_URL)
public class TransactionController {

    private LoggingHelper loggingHelper = LoggingHelper.getInstance();
    private final TransactionIdGenerator transactionIdGenerator;

    private final BasicAuthorizationUtil basicAuthorizationUtil;

    private final TransactionService transactionService;

    private final MappingUtility mappingUtility;


    @ApiOperation(value = "This operation is used to get transaction history of customer from system by scoring system",
            notes = "This is a get transaction history endpoint", response = TransactionDTO[].class)
    @GetMapping(value = "/{customerNumber}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> onboardurl(@Valid @PathVariable String customerNumber,
                                           @RequestHeader(defaultValue = "", value = "Authorization", required = true) String authorization,
                                           @RequestHeader(defaultValue = "", value = "transactionId", required = false) String transactionId
    ) {

        if(transactionId == null){
            transactionId =transactionIdGenerator.generateTransactionId(SystemName.MIDDLEWARE.getName());
        }

        log.info("{}::: transactionId getting the transaction history for customerNumber {}", transactionId, customerNumber);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(Constants.TRANSACTION_ID, transactionId);

        boolean checkAuth = basicAuthorizationUtil.isAuthorizationValid(authorization).block();

        if(!checkAuth){

            APIResponse authResponseError = basicAuthorizationUtil.returnUnauthAccess();

            return Mono.just( ResponseEntity.status(authResponseError.getError().getStatus())
                    .headers(responseHeaders).body(authResponseError));
        }

        TransactionsResponse transactionResponse = this.transactionService.getTransactionData(transactionId, customerNumber);

        if(transactionResponse != null){

            List<TransactionDTO> responseList = null;

            if(transactionResponse.getTransactions() != null) {
                log.info("{}::: transactionId succesfull soap call with response from CBS, transaction record count {}", transactionId, transactionResponse.getTransactions().size());

                responseList= this.mappingUtility.map2TransactionDtolist(transactionResponse.getTransactions());
            }

            return Mono.just( ResponseEntity.status(HttpStatus.OK)
                    .headers(responseHeaders).body(responseList));
        }
        else{

            List<TransactionDTO> emptyList = new ArrayList<>();

            return Mono.just( ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(responseHeaders).body(transactionResponse.getTransactions()));
        }
    }

}