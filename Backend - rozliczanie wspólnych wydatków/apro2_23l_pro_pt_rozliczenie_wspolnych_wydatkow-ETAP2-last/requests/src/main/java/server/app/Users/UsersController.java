package server.app.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.app.Transaction.TransactionRepository;
import server.app.requests.UserRegisterRequest;
import server.app.requests.changePasswordRequest;
import server.app.requests.getUserRequest;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@RestController
public class UsersController {

    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;

    /**
     *This class is used to communicate with users repository
     * @param repository
     * @param transactionRepository
     */
    UsersController(UsersRepository repository, TransactionRepository transactionRepository) {
        this.usersRepository = repository;
        this.transactionRepository = transactionRepository;
        repository.findAll();
    }


    /**
     * The response is all users form users repository
     * @return
     */
    @GetMapping("/users")
    List<Users> all() {
        return usersRepository.findAll();
    }

    /**
     * The response is certain user from users repository
     * @param urRequest request with certain users
     * @return User
     */
    @GetMapping("/users/{name}")
    ResponseEntity<Users> userByName(@RequestBody getUserRequest urRequest){
        Users result = usersRepository.findByName(urRequest.username);
        if(!isNull(result) && Objects.equals(result.getToken(), urRequest.token))
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes certain user from repository
     * @param urRequest  request with certain users
     */
    @PostMapping("/users/remove/{name}")
    ResponseEntity<String> removeUser(@RequestBody getUserRequest urRequest){
        usersRepository.delete(Objects.requireNonNull(userByName(urRequest).getBody()));
        ObjectMapper objectMapper = new ObjectMapper();
        List<Users> users;
        users = usersRepository.findAll();
        try {
            objectMapper.writeValue(new File("requests/src/main/resources/usersBackup.json"),users);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Registers new users and adds them to users repository and serialize them to the backup files
     * @param urRequest request with username and password
     * @return Created user
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/users/register")
    ResponseEntity<Users> register(@RequestBody UserRegisterRequest urRequest) throws NoSuchAlgorithmException {
        if(!isNull(usersRepository.findByName(urRequest.username)))
            return null;
        Users user;
        if(validateUserInput(urRequest.username) && validateUserInput(urRequest.password)) {
            user = new Users(urRequest.username, urRequest.password);
            usersRepository.save(user);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Users> users;
            users = usersRepository.findAll();
            try {
                objectMapper.writeValue(new File("requests/src/main/resources/usersBackup.json"),users);
            }catch (IOException e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    /**
     * Checks if user exists and if the password is correct
     * @param urRequest request with username and password
     * @return User
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/users/login")
    ResponseEntity<Users> login(@RequestBody UserRegisterRequest urRequest) throws NoSuchAlgorithmException{
        Users user;
        if(validateUserInput(urRequest.username) && validateUserInput(urRequest.password)){
            user = usersRepository.Login(urRequest.username, Users.hashPassword(urRequest.password));
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Changes user's password
     * @param cPRequest request with username, current password, new password, and authorization token
     * @return Users
     * @throws NoSuchAlgorithmException
     */
    @PatchMapping("/users/changePassword")
    ResponseEntity<Users> changePassword(@RequestBody changePasswordRequest cPRequest) throws NoSuchAlgorithmException{
        Users user;
        if(validateUserInput(cPRequest.username) && validateUserInput(cPRequest.currentPassword) && validateUserInput(cPRequest.newPassword)){
            user = usersRepository.Login(cPRequest.username, Users.hashPassword(cPRequest.currentPassword));
            if(user.getToken().equals(cPRequest.token))
            {
                usersRepository.getReferenceById(user.getId()).setHashedPasswd(cPRequest.newPassword);
                usersRepository.save(user);
                return new ResponseEntity<>(user,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /**
     * method used to validate the User's input: username or password
     * accepts only characters of English alphabet, digits and a dot
     * @param userInput the input from the user
     * @return false if the given input contains an illegal character, true otherwise
     */
    public boolean validateUserInput(String userInput){
        return userInput.matches("^[a-zA-Z0-9]+$");
    }
}
