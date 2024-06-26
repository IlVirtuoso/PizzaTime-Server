package com.pizzaidph2.pizzaidph2.service;

import com.pizzaidph2.pizzaidph2.model.Account;

import java.util.Optional;

public interface AmqpUserService {
    /***
     *
     * @param userIdToken
     * @return an account if the token is valid, an empty optional otherwise
     */
    Optional<Account> VerifyUserToken(String userIdToken);
    /***
     *
     * @param managerToken
     * @return an account if the token is valid, an empty optional otherwise
     */
    Optional<Account> VerifyManagerToken(String managerToken);

    /***
     *
     * @param managerId
     * @return the vat number manager by the account, empty if not present
     */
    Optional<String> GetVatForManager(Long managerId);
}
