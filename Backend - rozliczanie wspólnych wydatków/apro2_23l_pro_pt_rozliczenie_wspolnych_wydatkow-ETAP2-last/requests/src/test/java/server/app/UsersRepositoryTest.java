package server.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.app.Users.Users;
import server.app.Users.UsersRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.security.NoSuchAlgorithmException;


@DataJpaTest
public class UsersRepositoryTest {
    @Autowired private UsersRepository usersRepository;
    @Test
    void insertedComponentsAreNotNull(){
        assertThat(usersRepository).isNotNull();
    }
    @Test
    void ifSavedFindsByName() throws NoSuchAlgorithmException {
        usersRepository.save(new Users("szwedson1", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"));
        assertThat(usersRepository.findByName("szwedson1")).isNotNull();
    }
}
