package server.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.app.Transaction.Transaction;
import server.app.Transaction.TransactionGraph;
import server.app.Transaction.TransactionRepository;
import server.app.Users.Users;
import server.app.Users.UsersRepository;
import java.math.BigDecimal;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
public class testConfig {

    private static final Logger log = LoggerFactory.getLogger(testConfig.class);

    /**
     * This method deserializes users and transactions from backups json files and adds them to the repository
     * @param repository repository that contains all users object
     * @param transactionrepository repository that contains all transactions object
     */

    @Bean
    CommandLineRunner initDatabase(UsersRepository repository, TransactionRepository transactionrepository) {
        return args -> {
            Users sender = new Users("szwedson1", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
            Users receiver = new Users("mati", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
            repository.save(sender);
            repository.save(receiver);
            Transaction transaction = new Transaction(sender, receiver, new BigDecimal(30.0),LocalDate.now() );
            transactionrepository.save(transaction);
        };
    }


}
