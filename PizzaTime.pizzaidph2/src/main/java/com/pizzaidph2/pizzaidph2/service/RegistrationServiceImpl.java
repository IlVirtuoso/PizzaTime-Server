package com.pizzaidph2.pizzaidph2.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.pizzaidph2.pizzaidph2.Component.Auth0JWT;
import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.Component.InvalidEmailFormatException;
import com.pizzaidph2.pizzaidph2.Component.PWDUtilities;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.repository.HybernateAccountRepositoryImpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class RegistrationServiceImpl {

    @Autowired
    HybernateAccountRepositoryImpl2 repo;

    @Autowired
    GeneralService accountService;

    @Autowired
    JWTServiceImpl jwtUtility;

    @Autowired
    GenericResponse respUtilities;

    @Autowired
    PWDUtilities pwdUtilities;

    public boolean isAvaillableLoginIdService(String email) {
        Optional<Account> optAccount = repo.findByEmail(email);
        if (optAccount.isPresent()) {
            return false;
        } else return true;
    }


    /**
     * Servizio di registrazione standard nuovi utenti.
     * N.B.: questo servizio non va chiamato esternamente al restController associato.
     * Registra un nuovo utente con credenziali standard
     * @param account Oggetto JSON rappresentante l'account da registrare.
     *                Campi accettati:
     *                "username" -> Necessario
     *                "password" -> Necessario
     *                "firstName" -> Facoltativo
     *                "lastName"  -> Facoltativo
     *                Ulteriori campi vengono ignorati
     * @param completeUser DEBUG PURPOSE ONLY - valorizza il completamento dell'utente
     * @return un intero "statusCode" che rappresenta l'esito dell'operazione:
     *      0 -> registrazione avvenuta con successo e utente registrato con tutti i campi obbligatori.
     *      206 -> account pending registration, viene ritornato quando l'utente non ha completato tutti i campi obbligatori
     *          Nel campo regToken è disponibile un JWT per richiamare l'API di Registration Completion
     *      202 -> La password non rispetta la password policy
     *      400 -> Parametri obbligatori mancanti o non validi
     */
    public int registerService(Account account, boolean completeUser) {
        if(account!=null && (account.getUsername()!=null && !account.getUsername().isEmpty() && !account.getUsername().isBlank() )
                && ((account.getPassword()!=null && !account.getPassword().isEmpty() && !account.getPassword().isBlank())
                    || (account.getSocialIdentity()!=null))) {

            boolean checkFreeAccount = isAvaillableLoginIdService(account.getUsername().trim());
            if(checkFreeAccount){
                Account target = new Account();
                if(account.getUsername()!=null) {
                    try {
                        target.setEmail(account.getUsername().trim());
                        target.setUsername(account.getUsername().trim());
                    } catch (InvalidEmailFormatException e) {
                        System.out.println("INVALID EMAIL ADDRESS");
                        return respUtilities.INVALID_PARAMETER_CODE;
                    }
                }
                if(account.getPassword()!=null);
                    if(pwdUtilities.checkPasswordPolicy(account.getPassword().trim())){
                     String shaPwd = pwdUtilities.sha256Encoding(account.getPassword().trim());
                     if(shaPwd==null){
                         return respUtilities.INVALID_PASSWORD_CODE;
                     }else{
                         target.setPassword(shaPwd);
                     }
                    }else{return respUtilities.INVALID_PASSWORD_CODE;}

                if(account.getFirstName()!=null);
                    target.setFirstName(account.getFirstName());
                if(account.getLastName()!=null);
                    target.setLastName(account.getLastName());

                    int message = respUtilities.PENDING_REGISTRATION_CODE;;
                    if(completeUser) {
                        target.setRegistered(true);
                        message = respUtilities.OK_CODE;
                    }else {
                        target.setRegistered(false);
                    }
                repo.save(target);
                return message;
            }else{
                return respUtilities.INVALID_PARAMETER_CODE;
            }
        }else{return respUtilities.INVALID_PARAMETER_CODE;}
    }

    /**
     * Servizio di finalizzazione della registrazione.
     * N.B.: questo servizio non va chiamato esternamente al restController associato.
     * L'aunteticazione avviene solo mediante verifica del regToken.
     * L'API permette di settare le informazioni necessarie a completare il profilo e risultare regitrati completamente.
     * @param regToken token di autenticazione da passare nell'HEADER HTTP
     * @param account Oggetto JSON rappresentante l'account da registrare.
     *                Campi accettati:
     *                "firstName" -> Facoltativo
     *                "lastName"  -> Facoltativo
     *                "mobile" -> Facoltativo
     *                "phone" -> Facoltativo
     *                "address" -> Facoltativo
     *                Ulteriori campi vengono ignorati
     * @return un intero "statusCode" che rappresenta l'esito dell'operazione:
     *      0 -> Registrazione avvenuta con successon e auto login avvenuta con successo.
     *          Nel campo "sessionToken" della response viene passato un cookie di sessione.
     *      203 -> L'utente non ha completato la finalizzazione per mancanza di informazioni: ripetere.
     *      400 -> Parametri obbligatori mancanti o non validi. In particolare, l'utente è già registrato e non può chiamare questa API
     *      500 -> Errore Generico
     */
    public int completeRegistrationService(String regToken, Account account) {
        DecodedJWT jwt = jwtUtility.verifyRegToken(regToken);
        if(jwt!=null && !jwt.equals("")) {
            Account target = accountService.getInternalAccountInfo(Long.parseLong(jwt.getSubject()));
            if (target != null) {
                System.out.println("user found");

                if (!target.getRegistered()) {

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
                        //Finalize the registration
                        target.setRegistered(true);
                        repo.save(target);
                        return respUtilities.OK_CODE;
                    } else {
                        return respUtilities.MISSING_INFORMATION_CODE;
                    }
                } else return respUtilities.INVALID_PARAMETER_CODE;
            }
        }else{
            return respUtilities.FAILED_AUTHENTICATION_CODE;
        }

        return respUtilities.GENERIC_ERROR_CODE;
    }


    public int SocialRegisterService(Account account, boolean completeUser) {
        if(account!=null && (account.getUsername()!=null && !account.getUsername().isEmpty() && !account.getUsername().isBlank())) {

            boolean checkFreeAccount = isAvaillableLoginIdService(account.getUsername().trim());
            if(checkFreeAccount){
                Account target = new Account();
                if(account.getUsername()!=null) {
                    try {
                        target.setEmail(account.getUsername().trim());
                        target.setUsername(account.getUsername().trim());
                    } catch (InvalidEmailFormatException e) {
                        System.out.println("INVALID EMAIL ADDRESS");
                        return respUtilities.INVALID_PARAMETER_CODE;
                    }
                }

                target.setSocialIdentity(true);

                if(account.getFirstName()!=null);
                    target.setFirstName(account.getFirstName());
                if(account.getLastName()!=null);
                    target.setLastName(account.getLastName());

                int message = respUtilities.PENDING_REGISTRATION_CODE;;
                if(completeUser) {
                    target.setRegistered(true);
                    message = respUtilities.OK_CODE;
                }else {
                    target.setRegistered(false);
                }
                repo.save(target);
                return message;
            }else{
                return respUtilities.INVALID_PARAMETER_CODE;
            }
        }else{return respUtilities.INVALID_PARAMETER_CODE;}
    }

}
