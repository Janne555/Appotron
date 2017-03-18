package sql.db;

import java.util.*;
import storables.User;

public interface Dao<T, K> {

    T store(T t, User user) throws Exception;

    List<T> search(User user, Object... searchWords) throws Exception;

    T findOne(K key, User user) throws Exception;

    List<T> findAll(User user) throws Exception;

    void update(T t, User user) throws Exception;

    void delete(K key, User user) throws Exception;
}
