package tellerapp.accountservice.service;

import org.springframework.stereotype.Service;
import tellerapp.accountservice.entity.Account;
import tellerapp.accountservice.repository.AccountRepository;

import java.util.List;

@Service
public class AccountService {

    private static final String ACCOUNT_PREFIX = "BCC";
    private static final long START_ACCOUNT_NUMBER = 1000000001L;

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with account number: " + accountNumber));
    }

    public Account updateAccountByAccountNumber(String accountNumber, Account updatedAccount) {
        Account existingAccount = getAccountByAccountNumber(accountNumber);
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        return accountRepository.save(existingAccount);
    }

    public Account updateBalance(String accountNumber, double newBalance) {
        Account account = getAccountByAccountNumber(accountNumber);
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public void deleteAccountByAccountNumber(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        accountRepository.deleteById(account.getId());
    }

    private String generateAccountNumber() {
        List<String> accountNumbers = accountRepository.findLastAccountNumber();

        if (accountNumbers.isEmpty()) {
            return ACCOUNT_PREFIX + START_ACCOUNT_NUMBER;
        }

        String lastAccountNumber = accountNumbers.get(0);
        String numericPart = lastAccountNumber.replace(ACCOUNT_PREFIX, "");
        long nextAccountNumber = Long.parseLong(numericPart) + 1;

        return ACCOUNT_PREFIX + nextAccountNumber;
    }
}
