package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.ActivationToken;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

public interface ActivationTokenService {
    public ActivationToken createActivationTokenForUser(AppUser appUser);
    public ActivationToken getActivationToken(String activationToken);
}
