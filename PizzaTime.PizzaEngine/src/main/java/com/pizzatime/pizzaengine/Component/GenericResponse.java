package com.pizzatime.pizzaengine.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class GenericResponse {

    public static final int OK_CODE = 0;
    public static final String OK_MESSAGE = "OK";

    public static final int GENERIC_ERROR_CODE = 500;
    public static final String GENERIC_ERROR_MESSAGE = "General Server Error";

    public static final int INVALID_PARAMETER_CODE = 400;
    public static final String INVALID_PARAMETER_MESSAGE = "Invalid Parameter Value";

    public static final int FAILED_AUTHENTICATION_CODE = 401;
    public static final String FAILED_AUTHENTICATION_MESSAGE = "Wrong Authentication";

    public static final int PENDING_REGISTRATION_CODE = 206;
    public static final String PENDING_REGISTRATION_MESSAGE = "Account Pending Registration";

    public static final int NEW_SOCIAL_CODE = 207;
    public static final String NEW_SOCIAL_MESSAGE = "New Social User Must Be Registered Before Access";


    public static final int INVALID_PASSWORD_CODE = 202;
    public static final String INVALID_PASSWORD_MESSAGE = "Password doesn't meet complexity requirements";

    public static final int MISSING_INFORMATION_CODE = 203;
    public static final String MISSING_INFORMATION_MESSAGE = "Missing Required Informations";

    public static final int INVALID_LOGIN_CODE = 201;
    public static final String INVALID_LOGIN_MESSAGE = "Invalid login or Password";

    public static final int UNKNOWN_USER_CODE = 205;
    public static final String UNKNOWN_USER_MESSAGE = "Unknown User";

    public String mapCodeToResponse(int code){
        if(code==OK_CODE) return OK_MESSAGE;
        if(code==GENERIC_ERROR_CODE) return GENERIC_ERROR_MESSAGE;
        if(code==INVALID_PARAMETER_CODE) return INVALID_PARAMETER_MESSAGE;
        if(code==FAILED_AUTHENTICATION_CODE) return FAILED_AUTHENTICATION_MESSAGE;
        if(code==PENDING_REGISTRATION_CODE) return PENDING_REGISTRATION_MESSAGE;
        if(code==INVALID_PASSWORD_CODE) return INVALID_PASSWORD_MESSAGE;
        if(code==INVALID_LOGIN_CODE) return INVALID_LOGIN_MESSAGE;
        if(code==MISSING_INFORMATION_CODE) return MISSING_INFORMATION_MESSAGE;
        if(code==UNKNOWN_USER_CODE) return UNKNOWN_USER_MESSAGE;
        if(code==NEW_SOCIAL_CODE) return NEW_SOCIAL_MESSAGE;
        return GENERIC_ERROR_MESSAGE;
    }

    @Expose
    int statusCode;
    @Expose
    String statusReason;
    @Expose
    String time;
    @Expose
    String idToken;
    @Expose
    String sessionToken;
    @Expose
    String regToken;
    @Expose
    String customObj;
    @Expose
    JsonObject decodedToken;

    public GenericResponse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ZoneId zoneId = ZoneId.of("Europe/Rome");
        this.time = (LocalDateTime.ofInstant(Instant.ofEpochMilli((System.currentTimeMillis())),zoneId)).format(formatter).toString();
    }

    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIdToken(String id_token) {
        this.idToken = id_token;
    }

    public void setCustomObj(String customObj) {
        this.customObj = customObj;
    }

    public void setRegToken(String regToken) {
        this.regToken = regToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setDecodedToken(JsonObject decodedToken) {
        this.decodedToken = decodedToken;
    }

}
