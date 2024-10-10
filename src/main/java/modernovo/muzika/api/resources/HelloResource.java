package modernovo.muzika.api.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.SecurityRepository;
import modernovo.muzika.repositories.UserRepository;

@Path("/hellos")
public class HelloResource {

    @Inject
    private BandRepository repo;
    @Inject
    private SecurityRepository secRepo;
    @Inject
    private UserRepository userRepo;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public User getHello() {
        Studio studio = new Studio("ZhiliBiliStudio", "14 Miami str. ST California");
        repo.add(studio);
        System.out.println("DOING SHIT");
        User user = new User("Mitya", "xyjcoaqizdnvpudeohakivmenyagbkbwnkdbkbgvngvqokqb");
        userRepo.registerUser(user);

        user = userRepo.updateUser(user);
        return user;
    }

}
