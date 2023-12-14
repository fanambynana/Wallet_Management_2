package management.wallet.controller;

import management.wallet.model.Transaction;
import management.wallet.model.TransactionSave;
import management.wallet.service.TransactionCrudOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TransactionController {
    TransactionCrudOperation service;

    @GetMapping("/transactions")
    public List<TransactionSave> findAll() {
        return service.findAll();
    }
    @PutMapping("/transactions")
    public List<TransactionSave> saveAll(List<TransactionSave> toSave) {
        return service.saveAll(toSave);
    }
    @PutMapping("/transactions")
    public TransactionSave save(TransactionSave toSave) {
        return service.save(toSave);
    }
    @GetMapping("/transactions/{id}")
    public TransactionSave findById(@PathVariable int id) {
        return service.findById(id);
    }
}
