package com.cheng.application.dbrunner.db;


import com.cheng.application.dbrunner.DBRunner;

public interface TransactionNoRet {

    void apply(DBRunner db) throws Exception;

}
