package management.wallet.controller;

import management.wallet.model.AccountSave;
import management.wallet.service.AccountCrudOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AccountController {
    AccountCrudOperation service;

    @GetMapping("/accounts")
    public List<AccountSave> findAll() {
        return service.findAll();
    }
    @PutMapping("/accounts")
    public List<AccountSave> saveAll(List<AccountSave> toSave) {
        return service.saveAll(toSave);
    }
    @PutMapping("/accounts")
    public AccountSave save(AccountSave toSave) {
        return service.save(toSave);
    }
    @GetMapping("/accounts/{id}")
    public AccountSave findById(@PathVariable int id) {
        return service.findById(id);
    }
}
