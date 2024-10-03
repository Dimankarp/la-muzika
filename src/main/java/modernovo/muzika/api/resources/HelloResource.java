package modernovo.muzika.api.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import modernovo.muzika.model.Studio;
import modernovo.muzika.repositories.BandRepository;


@Path("/hellos")
public class HelloResource {

    @Inject
    private BandRepository repo;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Studio getHello() {
        Studio studio = new Studio("ZhiliBiliStudio", "14 Miami str. ST California");
        repo.add(studio);
        return studio;
    }

}
