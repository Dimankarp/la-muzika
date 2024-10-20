package modernovo.muzika.repositories;

import modernovo.muzika.model.Role;
import modernovo.muzika.model.User;

public interface RoleRepository {

    void addRole(User user, Role role);

}
