package server.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.query.Param;
import server.app.Transaction.Transaction;
import server.app.Users.Users;
import server.app.Users.UsersRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadDBTest {

    String setUp(){
        String result = "";
        File jasonUsersFile = new File("C:\\Users\\jakub\\Saved Games\\apro2_23l_pro_pt_rozliczenie_wspolnych_wydatkow\\requests\\src\\test\\java\\resources\\usersBackup.json");
        File jasonTransaction = new File("C:\\Users\\jakub\\Saved Games\\apro2_23l_pro_pt_rozliczenie_wspolnych_wydatkow\\requests\\src\\test\\java\\resources\\transactionBackup.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Users> usersList = objectMapper.readValue(jasonUsersFile, new TypeReference<List<Users>>() {
            });
            List<Transaction> transactionList = objectMapper.readValue(jasonTransaction, new TypeReference<List<Transaction>>() {
            });
            for (Users user : usersList) {
                result = result.concat("Preloading " + user.getId()+" "+user.getUsername()+" "+user.getBalance());
            }
            for (Transaction transaction : transactionList) {
                result = result.concat(" Preloading " + transaction.getSender().getUsername()+" " + transaction.getReceiver().getUsername()+" "+transaction.getAmount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Test
    void loaderTest(){
        String actual = setUp();
        assertEquals("Preloading 1 szwedson1 30.0Preloading 2 szwedson2 30.0 Preloading szwedson1 szwedson2 30.0", actual);
    }

}
