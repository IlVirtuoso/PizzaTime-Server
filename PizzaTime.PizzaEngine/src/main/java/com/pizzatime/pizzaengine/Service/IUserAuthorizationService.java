package com.pizzatime.pizzaengine.Service;

import java.util.Optional;

public interface IUserAuthorizationService { //should be the deserialized version of the token
    public Optional<UserAccount> validateUserIdToken(String token);
    public Optional<ManagerAccount> validateManagerIdToken(String token);
}
