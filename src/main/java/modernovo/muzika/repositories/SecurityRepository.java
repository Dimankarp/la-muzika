package modernovo.muzika.repositories;

import jakarta.data.repository.Insert;
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

    @SQL("insert into role_member(member_id, role) values (:id, :role)")
    void addRole(Long id, String role);

}
