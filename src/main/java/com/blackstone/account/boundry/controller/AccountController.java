
package com.blackstone.account.boundry.controller;

import com.blackstone.account.entity.dto.AccountDto;
import com.blackstone.account.control.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.createAccount(accountDto));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String accountId, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, accountDto));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-balance/{customerId}")
    public ResponseEntity<Boolean> hasNonZeroBalance(@PathVariable String customerId) {
        boolean hasBalance = accountService.hasNonZeroBalance(customerId);
        return ResponseEntity.ok(hasBalance);
    }
}
