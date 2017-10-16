package com.bombon.voxr.dao;

import com.bombon.voxr.model.User;

import io.realm.Realm;

/**
 * Created by Vaughn on 8/18/17.
 */

public class UserDao extends DaoImpl<User> {
    public UserDao() {
        super(User.class);
    }

    public static User profile(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(User.class).findFirst();
    }
}
