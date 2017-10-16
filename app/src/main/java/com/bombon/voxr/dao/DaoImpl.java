package com.bombon.voxr.dao;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Vaughn on 8/18/17.
 */

public abstract class DaoImpl<Type extends RealmObject> implements Dao<Type> {

    private Class<Type> type;
    private Realm realm = Realm.getDefaultInstance();

    public DaoImpl(Class<Type> type) {
        this.type = type;
    }

    @Override
    public List<Type> get() {
        RealmResults<Type> all = realm.where(type).findAll();

        return all.subList(0, all.size());
    }

    @Override
    public Type get(Integer id) {
        return realm.where(type).equalTo("id", id).findFirst();
    }

    @Override
    public Integer getId() {
        // increatement index
        Number nextID = (realm.where(type).max("id"));
        if (nextID == null) {
            return 1;
        }
        return nextID.intValue() + 1;
    }

    @Override
    public RealmModel save(final Type item) {
        realm.beginTransaction();

        RealmModel realmModel = realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();

        if (realmModel != null)
            return realmModel;
        else
            return null;
    }

    @Override
    public void save(Collection<Type> items) {
        realm.beginTransaction();
        for (Type item : items) {
            realm.copyToRealmOrUpdate(item);
        }
        realm.commitTransaction();
    }

    @Override
    public void clear() {
        realm.beginTransaction();
        RealmResults<Type> all = realm.where(type).findAll();
        all.deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void delete(final Integer id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Type> all = realm.where(type).equalTo("id", id).findAll();
                all.deleteAllFromRealm();
            }
        });
    }

    @Override
    public void delete(Collection<Type> items) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Type> all = realm.where(type).findAll();
                all.deleteAllFromRealm();
            }
        });
    }

    @Override
    public Integer count() {
        return get().size();
    }
}