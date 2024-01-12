package management.wallet.DAO;

import java.util.List;

public interface CrudOperation<T> {
    List<T> findAll();

    T findById(int id);

    List<T> saveAll(List<T> toSave);

    T save(T toSave);

    T update(T toUpdate);
}
