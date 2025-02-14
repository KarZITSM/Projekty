package server.app.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

@Entity
public class Users implements Serializable {
    private String username;
    private String hashedPasswd;

    private String token;
    public BigDecimal initialBalance = new BigDecimal(0);
    public BigDecimal balance = new BigDecimal(0);
    @Id @GeneratedValue
    private Long id;



    public String getHashedPasswd() {
        return hashedPasswd;
    }
    public Users(){}
    public Users(String username, String hashedPasswd, BigDecimal balance) {
        this.username = username;
        this.hashedPasswd = hashedPasswd;
        this.balance = balance;
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        this.token = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        this.initialBalance=initialBalance;
    }
    public Users(String username, String hashedPasswd) throws NoSuchAlgorithmException {
        this.username = username;
        this.hashedPasswd = hashPassword(hashedPasswd);
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        this.token = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        this.balance=getBalance();
        this.initialBalance=initialBalance;
    }


    public String getUsername() {
        return username;
    }


    public static String hashPassword(String passwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(passwd.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(Integer.toHexString((b & 0xFF) + 0x100).substring(1));
        }
        return sb.toString();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(username, users.username) && Objects.equals(hashedPasswd, users.hashedPasswd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, hashedPasswd);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hashedPasswd='" + hashedPasswd + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setHashedPasswd(String hashedPasswd) throws NoSuchAlgorithmException {
        this.hashedPasswd = hashPassword(hashedPasswd);
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public String getToken() {
        return token;
    }
}
