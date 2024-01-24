package management.wallet.DAO.reflect;

import java.util.List;

public interface CrudOperation<T> {
    List<T> findAll();
    T save(T toSave);
    T findById(int id);
}
