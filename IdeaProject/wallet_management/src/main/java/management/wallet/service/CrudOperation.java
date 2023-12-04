package management.wallet.service;

import java.util.List;

public interface CrudOperation<T> {
    List<T> findAll();

    List<T> saveAll(List<T> toSave);

    T save(T toSave);

    // T delete(T toDelete);
}
