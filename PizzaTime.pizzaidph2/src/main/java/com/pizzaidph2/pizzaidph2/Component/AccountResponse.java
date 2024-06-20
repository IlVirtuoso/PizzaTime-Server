package com.pizzaidph2.pizzaidph2.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.pizzaidph2.pizzaidph2.model.Account;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class AccountResponse implements Response{

    @Expose
    String statusCode;
    @Expose
    String statusReason;
    @Expose
    String time;
    @Expose
    Account account;
    @Expose
    String customFields;

    public AccountResponse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond((System.currentTimeMillis())),zoneId).format(formatter);
    }

    public AccountResponse(String statusCode, String statusReason, String time, Account account) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();
        this.statusCode = statusCode;
        this.statusReason = statusReason;
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond((System.currentTimeMillis())),zoneId).format(formatter);
        this.account=account;
        this.customFields = null;
    }

    public AccountResponse(String statusCode, String statusReason, String time, String customFields, Account account) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();
        this.statusCode = statusCode;
        this.statusReason = statusReason;
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond((System.currentTimeMillis())),zoneId).format(formatter);
        this.account=account;
        this.customFields = customFields;
    }


    public String jsonfy(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }


    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }
}
