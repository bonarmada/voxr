package com.bombon.voxr.dao;

import java.util.Collection;
import java.util.List;

import io.realm.RealmModel;

/**
 * Created by Vaughn on 8/18/17.
 */


public interface Dao<T> {

    List<T> get();

    T get(Integer id);

    Integer getId();

    RealmModel save(T item);

    void save(Collection<T> items);

    void clear();

    void delete(Integer id);

    void delete(Collection<T> items);

    Integer count();
}

