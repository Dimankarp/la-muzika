package modernovo.muzika.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import modernovo.muzika.model.Role;
import modernovo.muzika.repositories.SecurityRepository;
import modernovo.muzika.repositories.UserRepository;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@ApplicationScoped
public class UserIdentityStore implements IdentityStore {

    @Inject
    Logger logger;

    @Inject
    private SecurityRepository secRepo;
    @Inject
    private UserRepository userRepo;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        try {
            logger.info("Got user credential {}", credential.getCaller());
            if (userRepo.userExist(credential.getCaller())) {

                var user = userRepo.findUserByName(credential.getCaller());
                String userHash = secRepo.getUserHash(user.getId());
                String passHash = PasswordEncoder.encodeSHA384(credential.getPasswordAsString());

                logger.info("HashComparing {} | {}", userHash, passHash);

                if (userHash.equals(passHash)) {
                    List<Role> roles = secRepo.getUserRoles(user.getId());
                    return new CredentialValidationResult(credential.getCaller(), roles.stream().map(Enum::name).collect(Collectors.toSet()));
                } else {
                    return CredentialValidationResult.INVALID_RESULT;
                }
            } else {
                return CredentialValidationResult.INVALID_RESULT;
            }


        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }


}
