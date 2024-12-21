package tellerapp.accountservice.service;

import tellerapp.accountservice.entity.Account;
import tellerapp.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Account existingAccount = getAccountById(id);
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
