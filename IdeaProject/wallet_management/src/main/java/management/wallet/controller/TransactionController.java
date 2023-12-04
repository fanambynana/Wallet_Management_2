package management.wallet.controller;

import management.wallet.model.Account;
import management.wallet.model.Transaction;
import management.wallet.service.TransactionCrudOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TransactionController {
    TransactionCrudOperation service;

    @GetMapping("/transactions")
    public List<Transaction> findAll() {
        return service.findAll();
    }
    @PutMapping("/transactions")
    public List<Transaction> saveAll(List<Transaction> toSave) {
        return service.saveAll(toSave);
    }
    @PutMapping("/transactions")
    public Transaction save(Transaction toSave) {
        return service.save(toSave);
    }
    @GetMapping("/transactions/{id}")
    public Transaction findById(@PathVariable int id) {
        return service.findById(id);
    }
}
