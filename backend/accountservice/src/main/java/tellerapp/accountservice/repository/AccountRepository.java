package tellerapp.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tellerapp.accountservice.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query(value = "SELECT account_number FROM accounts ORDER BY CAST(SUBSTRING(account_number, 4) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    List<String> findLastAccountNumber();
}
