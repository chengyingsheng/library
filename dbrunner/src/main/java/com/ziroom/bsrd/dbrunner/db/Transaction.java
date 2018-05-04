package com.ziroom.bsrd.dbrunner.db;


import com.ziroom.bsrd.dbrunner.DBRunner;

public interface Transaction<T> {

    T apply(DBRunner db) throws Exception;

}
