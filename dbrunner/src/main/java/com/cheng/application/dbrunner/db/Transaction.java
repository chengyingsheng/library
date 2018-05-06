package com.cheng.application.dbrunner.db;


import com.cheng.application.dbrunner.DBRunner;

public interface Transaction<T> {

    T apply(DBRunner db) throws Exception;

}
