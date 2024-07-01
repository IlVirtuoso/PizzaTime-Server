package com.pizzaidph2.pizzaidph2.service;


import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.Component.InvalidEmailFormatException;
import com.pizzaidph2.pizzaidph2.Component.PWDUtilities;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.repository.HybernateAccountRepositoryImpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    HybernateAccountRepositoryImpl2 repo;

    @Autowired
    JWTServiceImpl jwtUtility;

    @Autowired
    PWDUtilities pwdUtilities;

    @Autowired
    GenericResponse respUtilities;

    @Autowired
    RegistrationServiceImpl regUtility;

    @Autowired
    GeneralService genService;

    /**
     * Servizio PARZIALE di Login standard per utenti già registrati
     * N.B.: questo servizio non va chiamato esternamente al restController associato.
     * @param username username (forma di indirizzo email) dell'utente
     * @param pwd password dell'utente
     * @return un intero "statusCode" che rappresneta l'esito dell'operazione:
     *      0 -> login avvenuta con successo.
     *          Nel campo "sessionToken" della response viene passato un cookie di sessione.
     *      206 -> login avvenuta con successo ma l'utente deve ancora completare la registrazione.
     *          Nel campo regToken è disponibile un JWT per richiamare l'API di Registration Completion
     *      201 -> username o password non validi
     */
    public int loginService(String username, String pwd) {
        long id = genService.findIDByEmail(username.trim());
        if (id != -1) {
            Account target = genService.getInternalAccountInfo(id);
            String passedPwd = pwdUtilities.sha256Encoding(pwd.trim());
            System.out.println("password provided "+passedPwd);
            String userPwd = target.getPassword();
            System.out.println("user password     "+userPwd);
            if (target.getUsername().equals(username.trim())  && passedPwd.equals(userPwd)) {

                if(target.getRegistered()){
                    return respUtilities.OK_CODE;}
                else{
                    return respUtilities.PENDING_REGISTRATION_CODE;
                }
            }
        }
        return respUtilities.INVALID_LOGIN_CODE;
    }

    /**
     * Servizio che ritorna le informazioni dell'utente autenticato
     * @param id id dell'utente di cui si richiedono le informazioni
     * @return una response contenente "statusCode" 0 e l'oggetto Account nell'omonimo campo, oppure "statusCode" 401 in caso di utenza inesistente.
     */
    public String getAccountInfoService(Long id) {
        GenericResponse resp = new GenericResponse();
        Optional<Account> target = repo.findById(id);
        if (target.isPresent()){
            resp.setStatusCode(GenericResponse.OK_CODE);
            // IT MUST NOT RETURN THE PASSWORD AND SENSITIVE INFO
            resp.setAccount(target.get().makeSafeCopy());
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            // IT MUST NOT RETURN THE PASSWORD AND SENSITIVE INFO
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Servizio che ritorna le informazioni dell'utente autenticato
     * @param id identitificativo univoco dell'utente da modificare
     * @param account Oggetto di tipo account contenente i nuovi valori da modificare
     * @return una response contenente il campo intero "statusCode" che può valere:
     *  0 -> l'account è stato modificato con successo
     *          il campo "Account" è valorizzato con l'oggetto account modificato
     *  400 -> la modifica richiesta non è valida
     *  401 -> autenticazione fallita: l'utente non esiste
     */
    public String setAccountInfoService(Long id, Account account){
        GenericResponse resp = new GenericResponse();
        Optional<Account> optTarget = repo.findById(id);
        if (optTarget.isPresent()){
            Account target = optTarget.get();

            if (account.getFirstName() != null && !account.getFirstName().isBlank() && !account.getFirstName().isEmpty())
                target.setFirstName(account.getFirstName().trim());

            if (account.getLastName() != null && !account.getLastName().isBlank() && !account.getLastName().isEmpty())
                target.setLastName(account.getLastName().trim());

            if (account.getMobile() != null && !account.getMobile().isBlank() && !account.getMobile().isEmpty())
                target.setMobile(account.getMobile().trim());

            if (account.getPhone() != null && !account.getPhone().isBlank() && !account.getPhone().isEmpty())
                target.setPhone(account.getPhone().trim());

            if (account.getAddress() != null && !account.getAddress().isBlank() && !account.getAddress().isEmpty())
                target.setAddress(account.getAddress().trim());

            if (target.getFirstName() != null && !target.getFirstName().isBlank() && !target.getFirstName().isEmpty()
                    && target.getLastName() != null && !target.getLastName().isBlank() && !target.getLastName().isEmpty()
                    && (target.getMobile() != null && !target.getMobile().isBlank() && !target.getMobile().isEmpty()
                    || target.getPhone() != null && !target.getPhone().isBlank() && !target.getPhone().isEmpty())
                    && target.getAddress() != null && !target.getAddress().isBlank() && !target.getAddress().isEmpty()) {
                target.setRegistered(true);
                repo.save(target);
                resp.setAccount(target.makeSafeCopy());
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp.jsonfy();
            }else{
                //User erase some important field, so the edit fails
                //resp.setAccount(target.makeSafeCopy());
                resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
                resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Servizio di ricarica del portafloglio
     * @param userId
     * @param value
     * @return
     */
    public String rechargeBalance(long userId, float value) {
        GenericResponse resp = new GenericResponse();
        Optional<Account> optTarget = repo.findById(userId);
        if (optTarget.isPresent() && optTarget.get().getRegistered()!=null && optTarget.get().getRegistered() && value > 0){
            Account target = optTarget.get();
            if(target.getBalance()==null){
                target.setBalance((float)0.0);
            }
            float newBalance = target.getBalance()+Math.round(value * 100.0f) / 100.0f;
            target.setBalance(newBalance);
            target = repo.save(target);
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            Account newOne = target.makeSafeCopy();
            resp.setAccount(newOne);
            return resp.jsonfy();
        }else{
            resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
            resp.setStatusReason(GenericResponse.GENERIC_ERROR_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Servizio di "pagamento" dal portafloglio
     * @param userId
     * @param value
     * @return
     */
    @Override
    public String chargeOnBalance(long userId, float value) {
        GenericResponse resp = new GenericResponse();
        Optional<Account> optTarget = repo.findById(userId);
        if (optTarget.isPresent() && optTarget.get().getRegistered()!=null && optTarget.get().getRegistered() && value > 0){
            Account target = optTarget.get();
            if(target.getBalance() >= Math.round(value * 100.0f) / 100.0f ){
                if(target.getBalance()==null){
                    target.setBalance((float)0.0);
                }
                float newBalance = target.getBalance() - Math.round(value * 100.0f) / 100.0f;
                target.setBalance(newBalance);
                target = repo.save(target);
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                Account newOne = target.makeSafeCopy();
                resp.setAccount(newOne);
                return resp.jsonfy();
            }else{
                resp.setStatusCode(GenericResponse.NOT_ENOUGH_MONEY_CODE);
                resp.setStatusReason(GenericResponse.NOT_ENOUGH_MONEY_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
            resp.setStatusReason(GenericResponse.GENERIC_ERROR_MESSAGE);
            return resp.jsonfy();
        }
    }


    /**
     * Servizio che ritorna un JWT general purpose (ID_TOKEN) per l'utente autenticato
     * L'ID token può essere passato a external-BE e validato per garantire l'autenticità della chiamata FE to external-BE
     * @param account per cui si richiede il jwt generico
     * @return una response contenente statusCode 0 ed il jwt nel campo "idToken", oppure statusCode 401.
     */
    public String getJWTService(Account account){
        GenericResponse resp = new GenericResponse();
        String jwt = jwtUtility.getJWT(account);
        if(jwt==null || jwt.isBlank() || jwt.isEmpty()){
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        } else{
            resp.setIdToken(jwt);
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Servizio per gestire la richiesta di cancellazione dell'utente autenticato
     * @param id identificativo univivoco dell'utente che si intende cancellare
     * @return una response contenente statusCode 0 se l'utente è stato eliminato, oppure statusCode 401 se l'utente non esiste.
     */
    public String deleteAccountService(Long id){
        GenericResponse resp = new GenericResponse();
        Optional<Account> target = repo.findById(id);
        if (target.isPresent()){
            repo.delete(target.get());
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }else{
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }
    }

    /**
     * Servizio di cambio password
     * @param id identificativo univivoco dell'utente a ci si intende cambiare password
     * @param oldPwd vecchia password dell'utente
     * @param newPwd nuova password dell'utente
     * @return una response contenente statusCode 0 se l'utente è stato eliminato, oppure statusCode 401 se l'utente non esiste,
     * oppure 202 se la newPassword non rispetta la password policy.
     */
    public String changePasswordService(long id, String oldPwd, String newPwd){
        GenericResponse resp = new GenericResponse();
        Optional<Account> target = repo.findById(id);
        if (target.isPresent()){

            if(target.get().getPassword()!=null);
            if(pwdUtilities.checkPasswordPolicy(newPwd.trim()) && pwdUtilities.sha256Encoding(newPwd.trim())!=null){
                String actualPwd = target.get().getPassword();
                String passedShaPwd = pwdUtilities.sha256Encoding(oldPwd.trim());
                if(passedShaPwd==null){
                    resp.setStatusCode(GenericResponse.INVALID_PASSWORD_CODE);
                    resp.setStatusReason(GenericResponse.INVALID_PASSWORD_MESSAGE);
                    return resp.jsonfy();
                }else if(passedShaPwd.equals(actualPwd)) {
                    String shaPwd = pwdUtilities.sha256Encoding(newPwd.trim());
                    target.get().setPassword(shaPwd);
                    repo.save(target.get());
                    resp.setStatusCode(GenericResponse.OK_CODE);
                    resp.setStatusReason(GenericResponse.OK_MESSAGE);
                    return resp.jsonfy();
                }
            }else{
                resp.setStatusCode(GenericResponse.INVALID_PASSWORD_CODE);
                resp.setStatusReason(GenericResponse.INVALID_PASSWORD_MESSAGE);
                return resp.jsonfy();
            }
        }else {
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
        resp.setStatusReason(GenericResponse.GENERIC_ERROR_MESSAGE);
        return resp.jsonfy();
    }

    /**
     * Servizio per la gestione della social login (con Google).
     * Permette anche di gestire la registrazione in caso di utente nuovo.
     * @param oauthToken il Token OAuth 2.0 fornito da Google
     * @return una stringa JSON GenericResponse con un codice numerico nel campo "statusCode" che rappresenta l'esito dell'operazione:
     *      0 -> login avvenuta con successo
     *          il campo "sessionToken" è valorizzato con il jwt di sessione
     *      206 -> account pending registration, l'utente non ha ancora inserito alcuni campi obbligatori.
     *      401 -> Autenticazione non valida, corrisponde ad un errore di validazione del token OAuth
     *      500 -> Errore Generico
     */
    public String socialLogin(String oauthToken){
        GenericResponse resp = new GenericResponse();
        Account candidate = jwtUtility.verifyOAuthToken(oauthToken);
        if(candidate!=null){
            long id = genService.findIDByEmail(candidate.getEmail());
            if(id!=-1){
                Account target = genService.getInternalAccountInfo(id);
                if((target.getSocialIdentity()!=null && target.getSocialIdentity()) && target.getRegistered()){
                    // SUCCESS
                    resp.setStatusCode(GenericResponse.OK_CODE);
                    resp.setStatusReason(GenericResponse.OK_MESSAGE);
                    resp.setSessionToken(jwtUtility.getSessionToken(target));
                    return resp.jsonfy();
                }else if((target.getSocialIdentity()!=null && target.getSocialIdentity()) && !target.getRegistered()){
                    // MISSING INFORMATION
                    resp.setStatusCode(GenericResponse.PENDING_REGISTRATION_CODE);
                    resp.setStatusReason(GenericResponse.PENDING_REGISTRATION_MESSAGE);
                    resp.setRegToken(jwtUtility.getRegToken(target));
                    resp.setAccount(target);
                    return resp.jsonfy();
                }else if((target.getSocialIdentity()==null || !target.getSocialIdentity()) && !target.getRegistered()){
                    // FIRST SOCIAL LOGIN FOR A STANDARD UNCOMPLETED ACCOUNT
                    target.setSocialIdentity(true);
                    target = repo.save(target);
                    resp.setStatusCode(GenericResponse.PENDING_REGISTRATION_CODE);
                    resp.setStatusReason(GenericResponse.PENDING_REGISTRATION_MESSAGE);
                    resp.setSessionToken(jwtUtility.getRegToken(target));
                    return resp.jsonfy();
                }else if((target.getSocialIdentity()==null || !target.getSocialIdentity()) && !target.getRegistered()){
                    // FIRST SOCIAL LOGIN FOR A STANDARD ACCOUNT
                    target.setSocialIdentity(true);
                    target = repo.save(target);
                    resp.setAccount(target);
                    resp.setStatusCode(GenericResponse.OK_CODE);
                    resp.setStatusReason(GenericResponse.OK_MESSAGE);
                    resp.setSessionToken(jwtUtility.getSessionToken(target));
                    return resp.jsonfy();
                }
            }else{
                // SOCIAL REGISTRATION
                // The user doesn't exist: it must be registered
                candidate.setSocialIdentity(true);
                regUtility.SocialRegisterService(candidate, false);
                resp.setAccount(candidate);
                resp.setRegToken(jwtUtility.getRegToken(genService.getInternalAccountInfo(genService.findIDByEmail(candidate.getEmail()))));
                resp.setStatusCode(GenericResponse.PENDING_REGISTRATION_CODE);
                resp.setStatusReason(GenericResponse.PENDING_REGISTRATION_MESSAGE);
                return resp.jsonfy();
            }
        }else{
            //FAILED AUTHENTICATION OF THE OAUTH TOKEN
            resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
            resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.GENERIC_ERROR_CODE);
        resp.setStatusReason(resp.GENERIC_ERROR_MESSAGE);
        return resp.jsonfy();
    }







    // START DEMO SERVICES

    public String setAccountInfoServiceDemo(Account account) {
        Optional<Account> optAccount = repo.findById(account.getId());
        if(optAccount.isPresent()) {

            Account target = optAccount.get();

            target.setFirstName(account.getFirstName());
            target.setLastName(account.getLastName());
            target.setMobile(account.getMobile());
            try {
                target.setEmail(account.getEmail());
            } catch (InvalidEmailFormatException e) {
                e.printStackTrace();
            }

            repo.save(target);

            return target.jsonfy();
        }else {
            return "Invalid Parameter Value";
        }
    }

    public String registerServiceDemo(Account account) {
        Account target = repo.save(account);
        //Account target = repo.save(new Account(account.getFirstName(), account.getLastName()));
        return account.jsonfy();
    }

    /* START DEBUG METHOD*/

    public String getJWTService(long exp){
        return jwtUtility.getJWT(exp, null);
    }


}
