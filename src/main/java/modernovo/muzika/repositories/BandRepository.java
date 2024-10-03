package modernovo.muzika.repositories;

import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Repository;
import modernovo.muzika.model.Book;
import modernovo.muzika.model.Studio;

@Repository
public interface BandRepository {


    @Insert
    void add(Studio studio);

}
