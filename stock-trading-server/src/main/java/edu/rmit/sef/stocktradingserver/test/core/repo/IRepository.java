package edu.rmit.sef.stocktradingserver.test.core.repo;

public interface IRepository<T, K> {

    void add(T object);

    void update(T object);

    void delete(K id);

    T findById(K id);

    T[] getAll(Paging page);

}
