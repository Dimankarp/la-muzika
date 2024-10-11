package modernovo.muzika.security;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;

import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;


@ApplicationScoped
public class LoginHeadersAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    Logger logger;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {

        logger.info("Validating request");
        if (!httpMessageContext.isProtected()) {
            logger.info("Not protected {}", request.getRequestURI());
            return httpMessageContext.doNothing();
        }

        var username = request.getHeader("username");
        var password = request.getHeader("password");

        logger.info("Got user credentials {} {}", username, password);

        if (username == null || password == null) {
            return httpMessageContext.responseUnauthorized();
        }

        var validationResult = identityStoreHandler.validate(new UsernamePasswordCredential(username, password));

        logger.info("Validated to {} ", validationResult.getStatus().toString());

        if (validationResult.getStatus() != CredentialValidationResult.Status.VALID) {
            return httpMessageContext.responseUnauthorized();
        }

        logger.info("Principal: {} {}", validationResult.getCallerPrincipal().getName(), validationResult.getCallerGroups().stream().reduce((x, y) -> x + " " + y).orElseThrow());

        return httpMessageContext.notifyContainerAboutLogin(validationResult);


    }
}
