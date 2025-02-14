package server.app;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import server.app.Transaction.Transaction;
import server.app.Transaction.TransactionRepository;
import server.app.Users.Users;
import server.app.Users.UsersRepository;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@DataJpaTest
public class TransactionRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);
    @Autowired private TransactionRepository transactionRepository;
    @Test
    void insertedComponentsAreNotNull(){
        assertThat(transactionRepository).isNotNull();
    }
    @Test
    void ifSavedFindsByReceiver() throws NoSuchAlgorithmException {
        Users receiver = new Users("mati", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        assertThat(transactionRepository.findByReceiver(receiver)).isNotNull();
    }
    @Test
    void ifSavedFindsBySender() throws NoSuchAlgorithmException {
        Users sender = new Users("szwedson1", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        Users receiver = new Users("mati", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
        Transaction transaction = new Transaction(sender, receiver, new BigDecimal(30.0),LocalDate.now() );
        transactionRepository.save(transaction);
        assertThat(transactionRepository.findByReceiver(sender)).isNotNull();
    }
}
