package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.ActivationToken;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.ActivationTokenRepository;
import pl.dmcs.blaszczyk.service.ActivationTokenService;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class ActivationTokenServiceImp implements ActivationTokenService {

    @Autowired
    ActivationTokenRepository activationTokenRepository;

    @Override
    public ActivationToken createActivationTokenForUser() {
        ActivationToken confirmationToken = new ActivationToken();
        Date currentDate = new Date();
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 1);
        confirmationToken.setExpirationDate(calendar.getTime());
        return confirmationToken;
    }

    @Override
    public ActivationToken getActivationToken(String activationToken) {
        if (activationToken == null) {
            throw new BadRequestException();
        }
        ActivationToken token = activationTokenRepository.findByConfirmationToken(activationToken);
        if (token == null) {
            throw new ResourceNotFoundException("Activation Token not found");
        }
        return token;
    }
}
