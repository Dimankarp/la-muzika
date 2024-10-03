package modernovo.muzika.api.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import modernovo.muzika.model.Book;
import modernovo.muzika.repositories.BookRepository;


@Path("/hellos")
public class HelloResource {

    @Inject
    private BookRepository repo;

    @GET
    public Book getHello() {
        Book tempBook = new Book();
        repo.add(tempBook);
        return new Book();
    }

}
