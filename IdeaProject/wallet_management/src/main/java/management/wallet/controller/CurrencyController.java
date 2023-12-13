package management.wallet.controller;

import management.wallet.service.CurrencyCrudOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import management.wallet.model.Currency;
import java.util.List;

@RestController
public class CurrencyController {
    CurrencyCrudOperation service;

    @GetMapping("/currencies")
    public List<Currency> findAll() {
        return service.findAll();
    }
    @PutMapping("/currencies")
    public List<Currency> saveAll(List<Currency> toSave) {
        return service.saveAll(toSave);
    }
    @PutMapping("/currencies")
    public Currency save(Currency toSave) {
        return service.save(toSave);
    }
    @GetMapping("/currencies/{id}")
    public Currency findById(@PathVariable int id) {
        return service.findById(id);
    }
}
