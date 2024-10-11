package modernovo.muzika.repositories;


import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import modernovo.muzika.model.User;


@Repository
public interface UserRepository {

    @Find
    User findUser(Long id);

    @Find
    User findUserByName(String username);

    @Query("select case when (count(u) > 0)  then true else false end " +
            "from User u where u.username = :username")
    Boolean userExist(String username);

    @Insert
    void registerUser(User user);
}
