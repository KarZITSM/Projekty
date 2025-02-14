package server.app.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.app.Users.Users;

import java.util.List;
public interface TransactionRepository extends JpaRepository<Transaction, Users>{

    //TODO: mplichta Queries should be based on string inputs rather than whole objects
    @Query("FROM Transaction WHERE id = :id")
    List<Transaction> findById(@Param("id") int id);
    @Query("FROM Transaction WHERE sender = :sender")
    List<Transaction> findBySender(@Param("sender") Users sender);

    @Query("FROM Transaction WHERE receiver = :receiver")
    List<Transaction> findByReceiver(@Param("receiver") Users receiver);

    @Query("FROM Transaction WHERE amount = :amount")
    List<Transaction> findByAmount(@Param("amount") double amount);

    @Query("FROM Transaction WHERE receiver.username = :name")
    List<Transaction> findByName(@Param("name") String name);
}
