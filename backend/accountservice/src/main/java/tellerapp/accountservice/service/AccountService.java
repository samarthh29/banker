package tellerapp.accountservice.service;

import tellerapp.accountservice.entity.Account;
import tellerapp.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private static final String ACCOUNT_PREFIX = "BCC"; // Define a prefix for account numbers
    private static final long START_ACCOUNT_NUMBER = 1000000001L; // Start from this number

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        // Generate the account number
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

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

    private String generateAccountNumber() {
        // Get the highest account number from the database
        String lastAccountNumber = accountRepository.findLastAccountNumber();

        if (lastAccountNumber == null) {
            // Start with the first account number if no accounts exist
            return ACCOUNT_PREFIX + START_ACCOUNT_NUMBER;
        }

        // Extract the numeric part of the last account number
        String numericPart = lastAccountNumber.replace(ACCOUNT_PREFIX, "");
        long nextAccountNumber = Long.parseLong(numericPart) + 1;

        // Return the new account number with the prefix
        return ACCOUNT_PREFIX + nextAccountNumber;
    }
}
