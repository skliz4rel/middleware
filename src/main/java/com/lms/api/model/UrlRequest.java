package com.lms.api.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UrlRequest {


    private String url;


    private String name;

    @NotNull(message = "Username cant be empty. Needed for Authorization")
    @NotEmpty(message="Username cant be empty. Needed for Authorization")
    private String username;

    @NotNull(message = "Password cant be empty. Needed for Authorization")
    @NotEmpty(message="Password cant be empty. Needed for Authorization")
    private String password;
}
