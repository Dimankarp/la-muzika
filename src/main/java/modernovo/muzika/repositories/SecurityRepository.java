package modernovo.muzika.repositories;

import jakarta.data.repository.Repository;
import modernovo.muzika.model.Role;
import org.hibernate.annotations.processing.SQL;

import java.util.List;


@Repository
public interface SecurityRepository {

    @SQL("select role from role_member where member_id = :id")
    List<Role> getUserRoles(Long id);

    @SQL("select hash from account where id = :id")
    String getUserHash(Long id);

}
