package server.app.Transaction;
import jakarta.persistence.*;
import server.app.Users.Users;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction {
    private @Id @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name="user_sender_id", nullable = false)
    private Users sender;

    @ManyToOne
    @JoinColumn(name="user_receiver_id", nullable = false)
    private Users receiver;
    private BigDecimal amount;
    private LocalDate date;

    public Transaction() {};

    public Transaction(Users sender, Users receiver, BigDecimal amount, LocalDate date) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
        TransactionGraph.addTransaction(this);
    }

    public Transaction(Long id, Users sender, Users receiver, BigDecimal amount, LocalDate date, boolean xd) {
        this.id=id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
    }

    public Users getSender() {
        return sender;
    }

    public Users getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public Long getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return "Transaction #" + id + " | " + sender.getUsername() + " -> " + receiver.getUsername() + " | Amount: " + amount;
    }
}

