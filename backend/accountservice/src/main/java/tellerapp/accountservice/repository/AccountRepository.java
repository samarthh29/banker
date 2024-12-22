package tellerapp.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tellerapp.accountservice.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);

    // JPQL query to fetch the latest account number
    @Query("SELECT a.accountNumber FROM Account a ORDER BY a.id DESC")
    List<String> findLastAccountNumber();
}