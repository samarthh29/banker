package tellerapp.accountservice.repository;

import tellerapp.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);

    // Query to get the highest account number from the database
    @Query("SELECT a.accountNumber FROM Account a ORDER BY a.accountNumber DESC LIMIT 1")
    String findLastAccountNumber();
}
