package modernovo.muzika.api.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.SecurityRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.security.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

@Path("/hellos")
public class HelloResource {

    @Inject
    private BandRepository repo;
    @Inject
    private SecurityRepository secRepo;
    @Inject
    private UserRepository userRepo;

    @Inject
    private SecurityContext secCtx;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getHello() {

        Principal princ = secCtx.getCallerPrincipal();
        if (princ == null) {
            return "Failed to create principal";
        } else {
            return princ.getName();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(@HeaderParam("username") String username, @HeaderParam("password") String password) {


        try {

            if (userRepo.userExist(username)) {
                return Response.ok()
                        .entity("User already exists")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();

            }
            User newUser = new User(username, PasswordEncoder.encodeSHA384(password));
            userRepo.registerUser(newUser);
            return Response.ok()
                    .entity("Success")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
