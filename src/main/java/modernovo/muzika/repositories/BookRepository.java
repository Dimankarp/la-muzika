package modernovo.muzika.repositories;

import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Repository;
import modernovo.muzika.model.Book;

@Repository
public interface BookRepository {
    @Find
    Book book(Long isbn);

    @Insert
    void add(Book book);

}
