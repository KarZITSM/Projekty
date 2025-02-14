package server.app.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.app.Users.Users;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users,Long> {
    @Query("FROM Users WHERE username = 'szwedson'")
    List<Users> findBySzwedson();

    @Query("FROM Users WHERE username = :name")
    Users findByName(@Param("name") String name);

    @Query("FROM Users WHERE username = :name AND hashedPasswd = :password")
    Users Login(@Param("name") String name,@Param("password") String password);
}
