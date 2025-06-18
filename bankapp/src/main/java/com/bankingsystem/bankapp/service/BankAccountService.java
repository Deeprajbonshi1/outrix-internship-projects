package com.bankingsystem.bankapp.service;

import com.bankingsystem.bankapp.entity.BankAccount;
import com.bankingsystem.bankapp.entity.Transaction;
import com.bankingsystem.bankapp.entity.User;
import com.bankingsystem.bankapp.exception.AccountNotFoundException;
import com.bankingsystem.bankapp.repository.BankAccountRepository;
import com.bankingsystem.bankapp.repository.TransactionRepository;
import com.bankingsystem.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankAccountService {
    private final BankAccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final UserRepository userRepo;

    @Autowired
    public BankAccountService(BankAccountRepository accountRepo,
                              TransactionRepository txRepo,
                              UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public BankAccount createAccount(User user, String name, double initialDeposit) {
        BankAccount account = new BankAccount();
        account.setAccountHolderName(name);
        account.setBalance(BigDecimal.valueOf(initialDeposit));
        account.setUser(user);
        account = accountRepo.save(account);

        // Update user's account reference
        user.setAccount(account);
        userRepo.save(user);

        if (initialDeposit > 0) {
            Transaction t = new Transaction();
            t.setAccount(account);
            t.setType("DEPOSIT");
            t.setAmount(BigDecimal.valueOf(initialDeposit));
            t.setBalanceAfterTransaction(account.getBalance()); // ADD THIS LINE
            txRepo.save(t);
        }
        return account;
    }

    @Transactional
    public BankAccount deposit(Long id, double amount) {
        BankAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));

        BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(amount));
        account.setBalance(newBalance);
        accountRepo.save(account);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setType("DEPOSIT");
        t.setAmount(BigDecimal.valueOf(amount));
        t.setBalanceAfterTransaction(newBalance); // ADD THIS LINE
        txRepo.save(t);

        return account;
    }

    @Transactional
    public BankAccount withdraw(Long id, double amount) {
        BankAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));

        if (account.getBalance().doubleValue() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        BigDecimal newBalance = account.getBalance().subtract(BigDecimal.valueOf(amount));
        account.setBalance(newBalance);
        accountRepo.save(account);

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setType("WITHDRAW");
        t.setAmount(BigDecimal.valueOf(amount));
        t.setBalanceAfterTransaction(newBalance); // ADD THIS LINE
        txRepo.save(t);

        return account;
    }
}