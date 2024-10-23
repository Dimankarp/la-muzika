package modernovo.muzika.repositories;


import modernovo.muzika.model.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>, RoleRepository {

    Optional<User> findByUsername(String username);
}
