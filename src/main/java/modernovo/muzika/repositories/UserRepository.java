package modernovo.muzika.repositories;


import jakarta.transaction.Transactional;
import modernovo.muzika.model.User;
import modernovo.muzika.model.UserDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>, RoleRepository {

    Optional<User> findByUsername(String username);

    @Query(value = "select new modernovo.muzika.model.UserDTO(ac.username) from User as ac where ac.username = :username")
    Optional<UserDTO> findUserDTOByName(String username);

}
