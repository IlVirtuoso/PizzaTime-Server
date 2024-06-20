package com.pizzaidph2.pizzaidph2.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pizzaidph2.pizzaidph2.Component.Auth0JWT;
import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTServiceImpl {

    @Autowired
    private Auth0JWT jwtUtility;

    /**
     * Crea un regToken (JWT) con scadenza di default per l'utente @account
     * @param account
     * @return un JWT codificato
     */
    public String getRegToken(Account account){
        return jwtUtility.getRegToken(account);
    }

    /**
     * Crea un sessionToken (JWT) con scadenza di default per l'utente @account
     * @param account
     * @return un JWT codificato
     */
    public String getSessionToken(Account account){
        return jwtUtility.getJWT(account,true);
    }

    /**
     * Crea un generico JWT con scadenza di default per l'utente @account
     * @param account
     * @return un JWT codificato
     */
    public String getJWT(Account account){
        return jwtUtility.getJWT(account,false);
    }

    /**
     * Crea un generico JWT con scadenza custom  per l'utente @account
     * @param account che il JWT identificherà
     * @param exp durata del JWT (in secondi)
     * @return un JWT codificato
     */
    public String getJWT(Long exp, Account account){
        return jwtUtility.getJWT(exp, account,false);
    }

    /**
     * Servizio di verifica completa di un regToken (JWT)
     * Un sessionToken deve sempre avere i claim "regTimestamp" e "isLoggedIn" (a false)
     * @param regToken
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifyRegToken(String regToken){
        return jwtUtility.verifyRegToken(regToken);
    }

    /**
     * Servizio di verifica completa di un sessionToken (JWT)
     * Un sessionToken deve sempre avere i claim "loginTimestamp" e "isLoggedIn" (a true)
     * @param sessionToken
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifySession(String sessionToken){
        return jwtUtility.verifySession(sessionToken);
    }

    /**
     * Servizio di verifica di validità di un token OAuth 2.0 di Google
     * @param oauthToken
     * @return un oggetto Account valorizzato con i dati indicati nel Google OAuth Token
     */
    public Account verifyOAuthToken(String oauthToken){
        return jwtUtility.verifyOAuthToken(oauthToken);
    }

    /**
     * Servizio di verifica completa di un generico JWT
     * Regole di validità:
     *  - Il JWT è stato staccato in uan data passata "iat" < NOW
     *  - Il JWT non è ancora scaduto > NOW
     *  - (non valutato) Il token non è stato ancora usato. "nbf" < NOW
     * @param jwt
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifyJWT(String jwt){
        return jwtUtility.verifyJWT(jwt);
    }

    /**
     * Servizio di verifica completa di un generico JWT
     * Regole di validità:
     *  - Il JWT è stato staccato in uan data passata "iat" < NOW
     *  - Il JWT non è ancora scaduto > NOW
     *  - (non valutato) Il token non è stato ancora usato. "nbf" < NOW
     * @param token
     * @return Il payload decodificato in JSON del JWT validato, o "null" altrimenti
     */
    public String verifyAndDecodeJWT(String token) {
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtUtility.verifyJWT(token);
        if(jwt!=null && !jwt.equals("")) {
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(jwt.getPayload()));
            Gson gson = new Gson();
            resp.setDecodedToken(gson.fromJson(payloadJson, JsonObject.class));
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

}
