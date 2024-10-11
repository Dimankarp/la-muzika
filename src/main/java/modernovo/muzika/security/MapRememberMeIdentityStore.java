package modernovo.muzika.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.credential.RememberMeCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.RememberMeIdentityStore;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MapRememberMeIdentityStore implements RememberMeIdentityStore {

    private final static int MAX_GENERATE_ATTEMPTS = 100;

    private final Map<String, CredentialValidationResult> tokenToIdentity = new ConcurrentHashMap<>();

    @Override
    public CredentialValidationResult validate(RememberMeCredential credential) {
        return tokenToIdentity.getOrDefault(credential.getToken(), CredentialValidationResult.INVALID_RESULT);
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> groups) {

        String token = null;
        for (int i = 1; i <= MAX_GENERATE_ATTEMPTS; i++) {
            token = UUID.randomUUID().toString();
            if (!tokenToIdentity.containsKey(token)) {
                break;
            }
        }

        if (token == null) {
            throw new IllegalStateException("Failed to generate unique token");
        }

        tokenToIdentity.put(token, new CredentialValidationResult(callerPrincipal, groups));
        return token;
    }

    @Override
    public void removeLoginToken(String token) {
        tokenToIdentity.remove(token);
    }
}
