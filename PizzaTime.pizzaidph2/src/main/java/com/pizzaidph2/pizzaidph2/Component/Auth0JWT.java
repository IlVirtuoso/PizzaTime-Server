package com.pizzaidph2.pizzaidph2.Component;
import com.pizzaidph2.pizzaidph2.model.Account;

import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import com.pizzaidph2.pizzaidph2.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;

@Component
public class Auth0JWT {

    @Autowired
    private AppProperties property;

    @Autowired
    private GeneralService generalUtilities;


    PublicKey publicrsa;
    PrivateKey privatersa;

    /*private String token;
    public String getBase64Value(){
        return this.token;
    }

    @Override
    public String toString() {
        return this.token;
    }
    */

    public Auth0JWT(AppProperties property){
        this.property = property;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.publicrsa = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(property.getRSAkeys_public())));
            System.out.println(Base64.getEncoder().encodeToString(publicrsa.getEncoded()));
            this.privatersa = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(property.getRSAkeys_private())));
            System.out.println(Base64.getEncoder().encodeToString(privatersa.getEncoded()));

        } catch (NoSuchAlgorithmException e) {
            System.out.println("MISSING RSA ALGORITHM");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            System.out.println("WRONG KEY TYPE");
            e.printStackTrace();
        }
    }

    /*
    public String getTestJWT(long exp){

        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)publicrsa,(RSAPrivateKey)privatersa);
            String token = JWT.create()

                    //Imposta l'issuer "iss" che sarà il nostro idp
                    .withIssuer(property.getIssuer())
                    //imposta il soggetto, l'UID dell'utente tipicamente, nel campo sub
                    .withSubject("1234@mail.com")
                    //set up a duration of one hour (300 seconds)
                    // Imposta il campo iat
                    .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis()/1000)))
                    //imposta la scadenza
                    .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis()/1000)+ exp))
                    //Set up the aud, which generally is receiver or multiple ones
                    .withAudience("BE_Orders","BE_Payements")
                    //Custom claims
                    .withClaim("user-type", "admin")
                    .withClaim("isLoggedIn", true)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            System.out.println("Invalid claims or key provided: impossible to create a new JWT");
            return null;
        }
    }
    */
    /**
     * This method verifies only the signature
     * @param token
     * @return "this" decoded or null
     */
    public DecodedJWT verifySignature(String token){
        DecodedJWT decodedJWT;
        try {
            //in theory we need only the private key to sign the JWT
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("pizza_party_idp")
                    // reusable verifier instance
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("Invalid signature or claims");
            return null;
        }
    }

    public String getJWT(){
        return getJWT((Long)property.getSessionExpiration(),null);
    }

    public String getJWT(Account account){
        return getJWT((Long)property.getSessionExpiration(), account, false);
    }

    /**
     * Create a custom JWT with a custom expiration @exp for user @account
     * @param exp
     * @param account
     * @return
     */
    public String getJWT(long exp, Account account){
        return getJWT(exp, account, false);
    }

    /**
     * Create a JWT as a Session Token with default expiration value for user @account
     * @param account
     * @param isSessionID
     * @return
     */
    public String getJWT(Account account, boolean isSessionID){
        return getJWT((Long)property.getSessionExpiration(), account, isSessionID);
    }

    public String getJWT(Long exp, Account account, boolean isSessionID) {

        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, (RSAPrivateKey) privatersa);

            if(isSessionID) {
                //This create a sessionID for a user, which is a particular JWT for user session
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZoneId zoneId = ZoneId.systemDefault();
                if(account.getVendor()!=null && account.getVendor()) {
                    // create a session token for a Pizzeria manager
                    long pizzaID = generalUtilities.findPizzeriaByManager(account.getId());
                    token = JWT.create()
                            .withIssuer(property.getIssuer())
                            .withSubject(account.getId().toString())
                            .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000)))
                            .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000) + exp))
                            .withClaim("isVendor",true)
                            .withClaim("pizzeriaID", pizzaID)
                            .withClaim("isLoggedIn", isSessionID)
                            // Only because I hate myself I put this in a single row ;)
                            .withClaim("loginTimestamp", LocalDateTime.ofInstant(Instant.ofEpochMilli((System.currentTimeMillis())), zoneId).format(formatter))
                            .sign(algorithm);
                }else{
                    token = JWT.create()
                            .withIssuer(property.getIssuer())
                            .withSubject(account.getId().toString())
                            .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000)))
                            .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000) + exp))
                            .withClaim("isLoggedIn", isSessionID)
                            // Only because I hate myself I put this in a single row ;)
                            .withClaim("loginTimestamp", LocalDateTime.ofInstant(Instant.ofEpochMilli((System.currentTimeMillis())), zoneId).format(formatter))
                            .sign(algorithm);
                }
            }else{
                //This create a common JWT for a certain user. It is useful to pass data to BE
                if(account.getVendor()!=null && account.getVendor()) {
                    // create a session token for a Pizzeria manager
                    long pizzaID = generalUtilities.findPizzeriaByManager(account.getId());
                    token = JWT.create()
                            .withIssuer(property.getIssuer())
                            .withSubject(account.getId().toString())
                            .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000)))
                            .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000) + exp))
                            .withClaim("isVendor",true)
                            .withClaim("pizzeriaID", pizzaID)
                            .sign(algorithm);
                }else {
                    token = JWT.create()
                            .withIssuer(property.getIssuer())
                            .withSubject(account.getId().toString())
                            .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000)))
                            .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000) + exp))
                            .sign(algorithm);
                }
            }
            return token;
        } catch (JWTCreationException exception) {
            System.out.println("Invalid claims or key provided: impossible to create a new JWT");
            return token;
        }
    }

        public String getRegToken(Account account) {

        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, (RSAPrivateKey) privatersa);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZoneId zoneId = ZoneId.systemDefault();
                token = JWT.create()
                        .withIssuer(property.getIssuer())
                        .withSubject(account.getId().toString())
                        .withIssuedAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000)))
                        .withExpiresAt(Instant.ofEpochSecond((System.currentTimeMillis() / 1000) + property.getRegTokenExpiration()))
                        .withClaim("isLoggedIn", false)
                        .withClaim("regTimestamp", LocalDateTime.ofInstant(Instant.ofEpochMilli((System.currentTimeMillis())),zoneId).format(formatter))
                        .sign(algorithm);
                //This create a common JWT for a certain user. It is useful to pass data to BE

            return token;
        } catch (JWTCreationException exception) {
            System.out.println("Invalid claims or key provided: impossible to create a new JWT");
            return token;
        }
    }

    /**
     * Metodo di verifica completa di un generico JWT
     * Regole di validità:
     *  - Il JWT è stato staccato in uan data passata "iat" < NOW
     *  - Il JWT non è ancora scaduto > NOW
     *  - (non valutato) Il token non è stato ancora usato. "nbf" < NOW
     * @param token
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifyJWT(String token){
        DecodedJWT decodedJWT;
        try {
            // We need only the public key to verify the signature
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer(property.getIssuer())
                    .acceptLeeway(1)   //1 sec for nbf and iat, if you leave only this parameter it applies also to exp
                    .acceptExpiresAt(2)   //5 secs for exp
                    .withClaimPresence("iat")
                    .withClaimPresence("exp")
                    .withClaimPresence("sub")
                    .build();
            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("WARNING: Invalid signature or claims");
            return null;
        }
    }


    /**
     * Metodo di verifica completa di un regToken (JWT)
     * Un sessionToken deve sempre avere i claim "regTimestamp" e "isLoggedIn" (a false)
     * @param token
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifyRegToken(String token){
        DecodedJWT decodedJWT;
        try {
            // We need only the public key to verify the signature
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(property.getIssuer())
                    .acceptLeeway(1)   //1 sec for nbf and iat, if you leave only this parameter it applies also to exp
                    .acceptExpiresAt(2)   //2 secs for exp
                    //check the presence of a certain claim
                    .withClaimPresence("iss")
                    .withClaimPresence("exp")
                    .withClaimPresence("sub")
                    .withClaimPresence("regTimestamp")
                    // require the claim isLoggedIn valued with "false"
                    .withClaim("isLoggedIn",false)
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("WARNING: Invalid signature or claims");
            return null;
        }
    }

    /**
     * Metodo di verifica completa di un sessionToken (JWT)
     * Un sessionToken deve sempre avere i claim "loginTimestamp" e "isLoggedIn" (a true)
     * @param token
     * @return un JWT decodificato o "null" se la validazione è fallita
     */
    public DecodedJWT verifySession(String token){
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicrsa, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(property.getIssuer())
                    .acceptLeeway(1)   //1 sec for nbf and iat, if you leave only this parameter it applies also to exp
                    .acceptExpiresAt(2)   //2 secs for exp
                    .withClaimPresence("iss")
                    .withClaimPresence("exp")
                    .withClaimPresence("sub")
                    .withClaimPresence("loginTimestamp")
                    // require the claim isLoggedIn valued with "true"
                    .withClaim("isLoggedIn",true)
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("WARNING: Invalid signature or claims");
            System.out.println(exception.getMessage());
            return null;
        }
    }

    /**
     * Metodo di verifica di validità di un token OAuth 2.0 di Google
     * @param oauthToken
     * @return un oggetto Account valorizzato con i dati indicati nel Google OAuth Token
     */
    public Account verifyOAuthToken(String oauthToken) {

        //Actually it support only Google

        Account a = new Account();

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(property.getGoogleClientID()))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(oauthToken);
        } catch (GeneralSecurityException e) {
            System.out.println("Invalid token");
        } catch (IOException e) {
            System.out.println("Input error");
        } catch (IllegalArgumentException e){
            System.out.println("Token is null, let the following handle the error");
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            Long exp = (Long) payload.get("exp");
            System.out.println("Expiration: " + exp);
            Long iat = (Long) payload.get("iat");
            System.out.println("Emission: " + iat);
            // Google JWT tipically expires in an hour
            System.out.println("Duration: " + (exp-iat));
            long actualTime = System.currentTimeMillis()/1000;
            System.out.println("Actual time: "+actualTime);
            //Manual check of the expiration
            if(actualTime>exp) {
                System.out.println("EXPIRED!\nActual time: "+actualTime+"\nExpiration time: "+exp);
                return null;
            }else{
                System.out.println("This is a valid Google JWT verified ");
            }

            /*
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            if(emailVerified){
                //NON CI CREDO... LE EMAIL UNIVERSITARIE NON SONO VERIFICATE
                System.out.println("USER IS NOT VERIFIED FROM GOOGLE");
                return null;
            }
            */

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            System.out.println("email: " + email);
            try {
                a.setEmail(email);
                a.setUsername(email);
            } catch (InvalidEmailFormatException e) {
                System.out.println("INVALID EMAIL RECEIVED FROM GOOGLE");
                return null;
            }
            /*
            String name = (String) payload.get("name");
            System.out.println("Name: " + name);
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            System.out.println("locale: " + locale);
            */

            String familyName = (String) payload.get("family_name");
            System.out.println("family name: " + familyName);
            a.setLastName(familyName);

            String givenName = (String) payload.get("given_name");
            System.out.println("givenName: " + givenName);
            a.setFirstName(givenName);

            return a;
        } else {
            System.out.println("Invalid Google ID token.");
            return null;
        }
    }
}
