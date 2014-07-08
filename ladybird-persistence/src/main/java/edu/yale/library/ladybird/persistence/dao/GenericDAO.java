package edu.yale.library.ladybird.persistence.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

    List<T> find(int rowNum, int count);

    List<T> findAll();

    Integer save(T entity);

    void saveOrUpdateItem(T item);

    void updateItem(T item);

    void saveOrUpdateList(List<T> itemList);

    int count();

    void delete(List<T> entities);
}
