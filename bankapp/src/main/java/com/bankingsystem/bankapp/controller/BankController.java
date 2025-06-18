package com.bankingsystem.bankapp.controller;

import com.bankingsystem.bankapp.dto.CreateAccountRequest;
import com.bankingsystem.bankapp.entity.BankAccount;
import com.bankingsystem.bankapp.entity.Transaction;
import com.bankingsystem.bankapp.entity.User;
import com.bankingsystem.bankapp.repository.TransactionRepository;
import com.bankingsystem.bankapp.repository.UserRepository;
import com.bankingsystem.bankapp.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // CORRECT IMPORT
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankController {
    private final BankAccountService accountService;
    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    @Autowired
    public BankController(
            BankAccountService accountService,
            TransactionRepository transactionRepo,
            UserRepository userRepo
    ) {
        this.accountService = accountService;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }
    @GetMapping("/test-auth")
    public String testAuth(Authentication authentication) {
        return "Authenticated as: " + authentication.getName();
    }

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(
            @Valid @RequestBody CreateAccountRequest req,
            Authentication authentication // Now using the correct Authentication interface
    ) {
        // Get current user
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BankAccount acct = accountService.createAccount(
                user, // Pass the user
                req.getAccountHolderName(),
                req.getInitialDeposit()
        );
        return ResponseEntity.ok(acct);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccount> deposit(@PathVariable Long id,
                                               @RequestParam double amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccount> withdraw(@PathVariable Long id,
                                                @RequestParam double amount) {
        return ResponseEntity.ok(accountService.withdraw(id, amount));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long id) {
        List<Transaction> transactions = transactionRepo.findByAccountId(id);
        return ResponseEntity.ok(transactions);
    }
}