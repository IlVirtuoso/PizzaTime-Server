package com.pizzaidph2.pizzaidph2.Component;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
public class AppProperties {

    private String issuer;
    private String RSAkeys_private;
    private String RSAkeys_public;

    private String GoogleClientID;

    private Long sessionExpiration;
    private Long regTokenExpiration;


    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getRSAkeys_private() {
        return RSAkeys_private;
    }

    public String getRSAkeys_public() {
        return RSAkeys_public;
    }

    public void setRSAkeys_private(String RSAkeysPrivate) {
        this.RSAkeys_private = RSAkeysPrivate;
    }

    public void setRSAkeys_public(String RSAkeysPublic) {
        this.RSAkeys_public = RSAkeysPublic;
    }

    public long getSessionExpiration() {
        return sessionExpiration;
    }

    public long getRegTokenExpiration() {
        return regTokenExpiration;
    }

    public void setSessionExpiration(long sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }

    public void setRegTokenExpiration(long regTokenExpiration) {
        this.regTokenExpiration = regTokenExpiration;
    }

    public String getGoogleClientID() {
        return GoogleClientID;
    }

    public void setGoogleClientID(String googleClientID) {
        GoogleClientID = googleClientID;
    }

    public void setSessionExpiration(Long sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }

    public void setRegTokenExpiration(Long regTokenExpiration) {
        this.regTokenExpiration = regTokenExpiration;
    }
}
