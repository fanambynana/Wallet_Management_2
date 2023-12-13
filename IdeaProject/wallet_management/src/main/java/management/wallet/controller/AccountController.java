package management.wallet.controller;

import management.wallet.model.Account;
import management.wallet.service.AccountCrudOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AccountController {
    AccountCrudOperation service;

    @GetMapping("/accounts")
    public List<Account> findAll() {
        return service.findAll();
    }
    @PutMapping("/accounts")
    public List<Account> saveAll(List<Account> toSave) {
        return service.saveAll(toSave);
    }
    @PutMapping("/accounts")
    public Account save(Account toSave) {
        return service.save(toSave);
    }
    @GetMapping("/accounts/{id}")
    public Account findById(@PathVariable int id) {
        return service.findById(id);
    }
}
