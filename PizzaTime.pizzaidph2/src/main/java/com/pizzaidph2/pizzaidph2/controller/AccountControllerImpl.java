package com.pizzaidph2.pizzaidph2.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/account/v1")
public class AccountControllerImpl {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private RegistrationServiceImpl regService;

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private GeneralService genService;

    @Autowired
    private PizzeriaService pizzeriaService;

    /**
     * Inner class per la Request della changePassword
     */
    public static class PwdRequest {
        public String oldPassword;
        public String newPassword;
    }

    /**
     * API di registrazione standard nuovi utenti.
     * Registra un nuovo utente con credenziali standard
     *
     * @param account Oggetto JSON rappresentante l'account da registrare.
     *                Campi accettati:
     *                "username" -> Necessario
     *                "password" -> Necessario
     *                "firstName" -> Facoltativo
     *                "lastName"  -> Facoltativo
     *                Ulteriori campi vengono ignorati
     * @return Un oggetto JSON response con un codice numerico nel campo "statusCode" che rappresenta l'esito dell'operazione:
     * 0 -> registrazione avvenuta con successo e utente registrato con tutti i campi obbligatori.
     * 206 -> account pending registration, viene ritornato quando l'utente non ha completato tutti i campi obbligatori
     * Nel campo regToken è disponibile un JWT per richiamare l'API di Registration Completion
     * 202 -> La password non rispetta la password policy
     * 400 -> Parametri obbligatori mancanti o non validi
     * 500 -> Errore Generico
     */
    @PostMapping("/register")
    public String register(@RequestBody() Account account) {

        GenericResponse resp = new GenericResponse();
        System.out.println("Start registration process");
        int result = regService.registerService(account, false);
        System.out.println("ended registration process");

        if (result == resp.PENDING_REGISTRATION_CODE) {
            long uid = genService.findIDByEmail(account.getUsername().trim());
            if (uid != -1) {
                System.out.println("Find user with UID " + uid);
                String regToken = jwtService.getRegToken(genService.getInternalAccountInfo(uid));
                System.out.println("I created the regToken" + regToken);
                resp.setStatusCode(resp.PENDING_REGISTRATION_CODE);
                resp.setStatusReason(resp.PENDING_REGISTRATION_MESSAGE);
                resp.setRegToken(regToken);
                resp.setAccount(genService.getInternalAccountInfo(uid).makeSafeCopy());
            } else {
                System.out.println("I wasnt able to find users with " + account.getEmail());
                resp.setStatusCode(resp.GENERIC_ERROR_CODE);
                resp.setStatusReason(resp.GENERIC_ERROR_MESSAGE);
            }
        } else {
            resp.setStatusCode(result);
            resp.setStatusReason(resp.mapCodeToResponse(result));
        }
        return resp.jsonfy();
    }

    /**
     * API di Login standard per utenti già registrati
     *
     * @param account Oggetto JSON rappresentante l'account da registrare.
     *                *                Campi accettati:
     *                *                "username" -> Necessario
     *                *                "password" -> Necessario
     *                *                Ulteriori campi vengono ignorati.
     * @return un JSON response con un codice numerico nel campo "statusCode" che rappresneta l'esito dell'operazione:
     * 0 -> login avvenuta con successo.
     * Nel campo "sessionToken" della response viene passato un cookie di sessione.
     * 206 -> login avvenuta con successo ma l'utente deve ancora completare la registrazione.
     * Nel campo regToken è disponibile un JWT per richiamare l'API di Registration Completion
     * 201 -> username o password non validi
     * 400 -> Parametri obbligatori mancanti o non validi
     * 500 -> Errore Generico
     */
    @PostMapping("/login")
    public String login(@RequestBody Account account) {
        GenericResponse resp = new GenericResponse();
        String username = account.getUsername().trim();
        String password = account.getPassword().trim();
        if (username != null && password != null) {
            int result = accountService.loginService(username.trim(), password.trim());
            if (result == resp.OK_CODE || result == resp.PENDING_REGISTRATION_CODE) {
                long id = genService.findIDByEmail(username.trim());
                if (id != -1) {
                    Account target = genService.getInternalAccountInfo(id);

                    if (result == resp.OK_CODE) {
                        System.out.println("User completed registration and logged in correctly");
                        String sessionToken = jwtService.getSessionToken(target);
                        //target.addSession(sessionToken);
                        //accountService.saveUser(target);
                        resp.setSessionToken(sessionToken);
                    }
                    if (result == resp.PENDING_REGISTRATION_CODE) {
                        System.out.println("User must complete registration before proceeding");
                        String regToken = jwtService.getRegToken(target);
                        resp.setRegToken(regToken);
                    }
                    resp.setStatusCode(result);
                    resp.setAccount(target.makeSafeCopy());
                    resp.setStatusReason(resp.mapCodeToResponse(result));
                    return resp.jsonfy();
                }
            } else {
                resp.setStatusCode(result);
                resp.setStatusReason(resp.mapCodeToResponse(result));
                return resp.jsonfy();
            }
        } else {
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(resp.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
        resp.setStatusReason(resp.GENERIC_ERROR_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API per la gestione della social login (con Google).
     * Permette anche di gestire la registrazione in caso di utente nuovo.
     *
     * @param oauthToken il Token OAuth 2.0 fornito da Google
     * @return un JSON response con un codice numerico nel campo "statusCode" che rappresenta l'esito dell'operazione:
     * 0 -> login avvenuta con successo
     * il campo "sessionToken" è valorizzato con il jwt di sessione
     * 206 -> account pending registration, l'utente non ha ancora inserito alcuni campi obbligatori.
     * 401 -> Autenticazione non valida, corrisponde ad un errore di validazione del token OAuth
     * 500 -> Errore Generico
     */
    @PostMapping("/socialLogin")
    public String login(@RequestHeader(value = "Authorization", required = false) String oauthToken) {
        String response = accountService.socialLogin(oauthToken);
        return response;
    }

    /**
     * API di finalizzazione della registrazione.
     * L'aunteticazione avviene solo mediante verifica del regToken.
     * L'API permette di settare le informazioni necessarie a completare il profilo e risultare regitrati completamente.
     *
     * @param regToken token di autenticazione da passare nell'HEADER HTTP
     * @param account  Oggetto JSON rappresentante l'account da registrare.
     *                 Campi accettati:
     *                 "firstName" -> Facoltativo
     *                 "lastName"  -> Facoltativo
     *                 "mobile" -> Facoltativo
     *                 "phone" -> Facoltativo
     *                 "address" -> Facoltativo
     *                 Ulteriori campi vengono ignorati
     * @return un JSON response con un codice numerico nel campo "statusCode" che rappresenta l'esito dell'operazione:
     * 0 -> Registrazione avvenuta con successon e auto login avvenuta con successo.
     * Nel campo "sessionToken" della response viene passato un cookie di sessione.
     * 203 -> L'utente non ha completato la finalizzazione per mancanza di informazioni: ripetere.
     * 400 -> Parametri obbligatori mancanti o non validi. In particolare, l'utente è già registrato e non può chiamare questa API
     * 401 -> Autenticazione non valida, corrisponde ad un errore di validazione del regToken
     * 500 -> Errore Generico
     */
    @PostMapping("/finalizeRegistration")
    public String finalizeRegistration(@RequestHeader(value = "Authorization", required = false) String regToken,
                                       @RequestBody() Account account) {

        GenericResponse resp = new GenericResponse();

        int result = regService.completeRegistrationService(regToken, account);

        if (result == resp.OK_CODE) {
            Account target = genService.getInternalAccountInfo(Long.parseLong(jwtService.verifyRegToken(regToken).getSubject()));
            if (target != null) {
                // User completed registration and logged in correctly
                String sessionToken = jwtService.getSessionToken(target);
                //target.addSession(sessionToken);
                //accountService.saveUser(target);
                resp.setSessionToken(sessionToken);
                resp.setStatusCode(result);
                resp.setStatusReason(resp.mapCodeToResponse(result));
                return resp.jsonfy();
            }
        } else {
            resp.setStatusCode(result);
            resp.setStatusReason(resp.mapCodeToResponse(result));
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
        resp.setStatusReason(resp.GENERIC_ERROR_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API che ritorna le informazioni dell'utente autenticato
     *
     * @param sessionToken da passare nel campo "Authorization" del request header
     * @return una response contenente statusCode 0 e l'oggetto "Account", oppure statusCode 401 (autenticazione fallita).
     */
    @GetMapping("/getAccountInfo")
    public String getAccountInfo(@RequestHeader(value = "Authorization", required = false) String sessionToken) {

        GenericResponse resp = new GenericResponse();

        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.getAccountInfoService(Long.parseLong(jwt.getSubject()));
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API che ritorna un JWT general purpose (ID_TOKEN) per l'utente autenticato
     * L'ID token può essere passato a external-BE e validato per garantire l'autenticità della chiamata FE to external-BE
     *
     * @param sessionToken campo Authorization del request header
     * @return una response contenente statusCode 0 ed il jwt nel campo "idToken", oppure statusCode 401.
     */
    @GetMapping("/getJWT")
    public String getJWT(@RequestHeader(value = "Authorization", required = false) String sessionToken) {

        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            Account target = genService.getInternalAccountInfo(Long.parseLong(jwt.getSubject()));
            return accountService.getJWTService(target);
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API che verifica un JWT general purpose (ID_TOKEN) per l'utente autenticato
     *
     * @param token per cui si richiede la verifica
     * @return una response contenente statusCode 0 ed il jwt con payload decodificato nel campo "decodedToken", oppure statusCode 401.
     */
    @GetMapping("/verifyJWT")
    public String verifyJWT(@RequestParam(value = "jwt", required = false) String token) {
        return jwtService.verifyAndDecodeJWT(token);
    }

    /**
     * API per settare  le informazioni di un utente autenticato.
     * L'API permette di settare le informazioni indicate nel body  profilo
     *
     * @param sessionToken token di autenticazione da passare nell'HEADER HTTP
     * @param account      Oggetto JSON rappresentante l'account da registrare.
     *                     Campi accettati:
     *                     "firstName" -> Facoltativo
     *                     "lastName"  -> Facoltativo
     *                     "mobile" -> Facoltativo
     *                     "phone" -> Facoltativo
     *                     "address" -> Facoltativo
     *                     Ulteriori campi vengono ignorati
     * @return un JSON response con un codice numerico nel campo "statusCode" che rappresenta l'esito dell'operazione:
     * 0 -> info modifcate con successo (l'API non setta valori vuoti o nulli)
     * 401 -> Autenticazione fallita: l'utente non esiste o il JWT non è valido
     * 400 -> Parametri obbligatori mancanti o non validi.
     * 500 -> Errore Generico
     */
    @PostMapping("/setAccountInfo")
    public String setAccountInfo(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                 @RequestBody() Account account) {

        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.setAccountInfoService(Long.parseLong(jwt.getSubject()), account);
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API per gestire la richiesta di cancellazione dell'utente autenticato
     *
     * @param sessionToken campo Authorization del request header
     * @return una response contenente statusCode 0 se l'utente è stato eliminato, oppure statusCode 401 (autenticazione fallita).
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(@RequestHeader(value = "Authorization", required = false) String sessionToken) {

        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.deleteAccountService(Long.parseLong(jwt.getSubject()));
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API di cambio password
     *
     * @param sessionToken
     * @param pwdData
     * @return
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                 @RequestBody() PwdRequest pwdData) {
        //System.out.println(pwdData.newPassword);
        //System.out.println(pwdData.oldPassword);
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.changePasswordService(Long.parseLong(jwt.getSubject()), pwdData.oldPassword.trim(), pwdData.newPassword.trim());
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API di ricarica del portafoglio
     *
     * @param sessionToken
     * @param value
     * @return
     */
    @GetMapping("/rechargeBalance")
    public String rechargeBalance(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                  @RequestParam(value = "value") float value) {
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.rechargeBalance(Long.parseLong(jwt.getSubject()), value);
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * API di aggiornamento del portafoglio per pagamento
     *
     * @param sessionToken
     * @param value
     * @return
     */
    @GetMapping("/chargeOnBalance")
    public String chargeOnBalance(@RequestHeader(value = "Authorization", required = false) String sessionToken,
                                  @RequestParam(value = "value") float value) {
        GenericResponse resp = new GenericResponse();
        DecodedJWT jwt = jwtService.verifySession(sessionToken);
        if (jwt != null && !jwt.equals("")) {
            return accountService.chargeOnBalance(Long.parseLong(jwt.getSubject()), value);
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }


    // this method will not be exposed

    /**
     * API per check disponibilità dello username richiesto.
     * N.B.: Si consiglia di non esporre questo endpoint per evitare account enumeration
     * (lo usiamo solo per DEBUG)
     *
     * @param email
     * @return true se la email non è associata ad alcun utente, false altrimenti.
     */
    @GetMapping("/v0/isAvaillableLoginID")
    public boolean isAvaillableLoginId(@RequestParam(name = "loginId") String email) {
        return regService.isAvaillableLoginIdService(email);
    }


    // START DEBUG METHOD

    @GetMapping("/v0/getAccountInfo")
    public String getAccountInfoDemo(@RequestParam(name = "id") Long id) {
        String target = accountService.getAccountInfoService(id);
        return target;
    }

    @PostMapping("/v0/setAccountInfo")
    public String setAccountInfo(@RequestBody() Account account) {
        String result = accountService.setAccountInfoServiceDemo(account);
        return result;
    }

    @GetMapping("/v0/getJWT")
    public String getJWT() {
        String jwt = accountService.getJWTService(60);
        if (jwt == null) {
            return "Invalid Parameter";
        } else {
            return jwt;
        }
    }

    @GetMapping("/getHelloWorld")
    public String getHelloWorld(@RequestParam(name = "print") boolean print) {
        GenericResponse resp = new GenericResponse();
        if (print) {
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            resp.setCustomObj("You get an Helloworld");
            return resp.jsonfy();
        } else {
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp.jsonfy();
        }
    }

    @PostMapping("/v0/registerDemo")
    public String registerDemo(@RequestBody() Account account) {
        String result = accountService.registerServiceDemo(account);
        return result;
    }

    @PostMapping("/postHelloWorld")
    public String postHelloWorld(@RequestBody() Boolean print) {
        if (print) {
            return "{String:\"Helloworld in POST!\"}";
        } else {
            return "Niente saluti";
        }
    }

    @PostMapping("/registerComplete")
    public String registerFinalize(@RequestBody() Account account) {

        GenericResponse resp = new GenericResponse();
        System.out.println("Start registration process");
        int result = regService.registerService(account, true);
        System.out.println("ended registration process");

        if (result == resp.PENDING_REGISTRATION_CODE || result == resp.OK_CODE) {
            long uid = genService.findIDByEmail(account.getUsername().trim());
            if (uid != -1) {
                System.out.println("Find user with UID " + uid);
                if (result == resp.PENDING_REGISTRATION_CODE) {
                    String regToken = jwtService.getRegToken(genService.getInternalAccountInfo(uid));
                    System.out.println("I created the regToken" + regToken);
                    resp.setStatusCode(resp.PENDING_REGISTRATION_CODE);
                    resp.setStatusReason(resp.PENDING_REGISTRATION_MESSAGE);
                    resp.setRegToken(regToken);
                }
                resp.setAccount(genService.getInternalAccountInfo(uid));
                resp.setSessionToken(jwtService.getSessionToken(genService.getInternalAccountInfo(uid)));
            } else {
                System.out.println("I wasnt able to find users with " + account.getEmail());
                resp.setStatusCode(resp.GENERIC_ERROR_CODE);
                resp.setStatusReason(resp.GENERIC_ERROR_MESSAGE);
            }
        } else {
            resp.setStatusCode(result);
            resp.setStatusReason(resp.mapCodeToResponse(result));
        }
        return resp.jsonfy();
    }

    @GetMapping("/populateAccountDB")
    public String populateAccountDB() {
        regService.populateAccountDB();
        pizzeriaService.createPizzeriaDemo();
        return "OK";
    }


}
