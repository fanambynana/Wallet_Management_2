package management.wallet.DAO.reflect;

public interface CrudOperation<T> {
    T findAll();
    T save(T toSave);
}
