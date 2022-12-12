package com.lms.api.service;

import com.lms.api.constants.NotificationStatusCode;
import com.lms.api.models.error.APIError;
import com.lms.api.models.response.APIResponse;
import com.lms.api.repositorys.TokenRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class BasicAuthorizationUtil {

    private final TokenRepository tokenRepository;

    public Mono<Boolean> isAuthorizationValid(String authData){

        if( StringUtils.isEmpty(authData)){
            return Mono.just(false);
        }

        if(!authData.contains("Basic ")){
            return Mono.just(false);
        }

        authData = authData.replace("Basic ","");


        String finalAuthData = authData.trim();
        return tokenRepository.findByBasicAuthstr(finalAuthData).map(item->{

            boolean check = item.getBasicAuthstr().equals(finalAuthData != null ? finalAuthData : "");

            return check;
        }).switchIfEmpty(Mono.fromCallable(() -> {

            return false;
        }));
    }


    public APIResponse returnUnauthAccess(){

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(NotificationStatusCode.CLIENT_UNAUTHORIZED.getCode());

        APIError apiError = new APIError();
        apiError.setTimestamp(LocalDateTime.now().toString());
        apiError.setStatusCode(NotificationStatusCode.CLIENT_UNAUTHORIZED.getCode());
        apiError.setStatus(HttpStatus.UNAUTHORIZED);
        apiError.setStatusMessage(NotificationStatusCode.CLIENT_UNAUTHORIZED.getDescription());

        apiResponse.setError(apiError);

        return apiResponse;
    }

}
