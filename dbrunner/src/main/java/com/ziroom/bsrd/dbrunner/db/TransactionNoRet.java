package com.ziroom.bsrd.dbrunner.db;


import com.ziroom.bsrd.dbrunner.DBRunner;

public interface TransactionNoRet {

    void apply(DBRunner db) throws Exception;

}
