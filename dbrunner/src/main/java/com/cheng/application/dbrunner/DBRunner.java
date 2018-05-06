package com.cheng.application.dbrunner;

import com.alibaba.druid.pool.DruidDataSource;
import com.cheng.application.basic.Predef;
import com.cheng.application.datatimes.TimeUtil;
import com.cheng.application.dbrunner.db.DBRunnerWithOp;
import com.cheng.application.dbrunner.db.Transaction;
import com.cheng.application.dbrunner.db.TransactionNoRet;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DBRunner extends DBRunnerWithOp {

    /**
     * 为与spring保持兼容，保留此构造函数
     */
    @Deprecated
    public DBRunner(String name, DataSource ds) {
        this(name, ds, false);
    }

    protected DBRunner(String name, DataSource ds, boolean inTransaction) {
        super(name, ds, !inTransaction);
        this.inTransaction = inTransaction;
    }

    /* ------------------------- builder ------------------------- */

    public static DBRunner of(String name, DataSource ds) {
        return new DBRunner(name, ds);
    }

    public static DBRunner of(String name, String host, int port, String dbName, String user, String pass) {
        return of(name, host, port, dbName, user, pass, defMaxConn);
    }

    public static DBRunner of(String name, String host, int port, String dbName, String user, String pass, int maxConn) {
        return of(name, getDataSource(host, port, dbName, user, pass, maxConn));
    }

    public static DBRunner of(String name, String url, String user, String pass) {
        return of(name, getDataSource(url, user, pass, 10));
    }

    public static final int defMaxConn = 100;

    private static DataSource getDataSource(String url, String user, String pass, int maxConn) {
        // ref: http://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html
        String jdbcUri = "jdbc:mysql://%s:%s/%s?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
//	String url = f(jdbcUri, host, port, dbName);

        // ref: https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_DruidDataSource%E5%8F%82%E8%80%83%E9%85%8D%E7%BD%AE
        try {
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pass);

            ds.setInitialSize(1);
            ds.setMinIdle(1);
            ds.setMaxActive(maxConn);
            ds.setMaxWait(1 * TimeUtil.minute);

            ds.setTimeBetweenEvictionRunsMillis(1 * TimeUtil.minute);
            ds.setMinEvictableIdleTimeMillis(5 * TimeUtil.minute);

            ds.setValidationQuery("select 'x'");
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);

            ds.setPoolPreparedStatements(true);
            ds.setMaxPoolPreparedStatementPerConnectionSize(20);

            ds.addFilters("mergeStat,wall");
            ds.addConnectionProperty("druid.stat.slowSqlMillis", "3000");

            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeoutMillis(30 * TimeUtil.minute);

            ds.init();
            return ds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource getDataSource(String host, int port, String dbName, String user, String pass, int maxConn) {
        // ref: http://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html
        String jdbcUri = "jdbc:mysql://%s:%s/%s?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
        String url = Predef.f(jdbcUri, host, port, dbName);

        // ref: https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_DruidDataSource%E5%8F%82%E8%80%83%E9%85%8D%E7%BD%AE
        try {
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pass);

            ds.setInitialSize(1);
            ds.setMinIdle(1);
            ds.setMaxActive(maxConn);
            ds.setMaxWait(1 * TimeUtil.minute);

            ds.setTimeBetweenEvictionRunsMillis(1 * TimeUtil.minute);
            ds.setMinEvictableIdleTimeMillis(5 * TimeUtil.minute);

            ds.setValidationQuery("select 'x'");
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);

            ds.setPoolPreparedStatements(true);
            ds.setMaxPoolPreparedStatementPerConnectionSize(20);

            ds.addFilters("mergeStat,wall");
            ds.addConnectionProperty("druid.stat.slowSqlMillis", "3000");

            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeoutMillis(30 * TimeUtil.minute);

            ds.init();
            return ds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* ------------------------- transaction ------------------------- */

    public final boolean inTransaction;

    public void ensureInTransaction() {
        if (!inTransaction) throw new IllegalStateException("not in transaction");
    }

    public void transaction(TransactionNoRet tx) throws Exception {
        transaction(tx, Connection.TRANSACTION_SERIALIZABLE);
    }

    public <T> T transaction(Transaction<T> tx) throws Exception {
        return transaction(tx, Connection.TRANSACTION_SERIALIZABLE);
    }

    public void transaction(final TransactionNoRet tx, int isolation) throws Exception {
        transaction(new Transaction<Void>() {
            public Void apply(DBRunner db) throws Exception {
                tx.apply(db);
                return null;
            }
        }, isolation);
    }

    public <T> T transaction(Transaction<T> tx, int isolation) throws Exception {
        final Connection conn = this.prepareConnection();
        int oldIsolation = -1;
        boolean commitSucc = false;

        try {
            conn.setAutoCommit(false);
            oldIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(isolation);

            DBRunner runner = new DBRunner(this.name, this.getDataSource(), true) {
                @Override
                protected Connection prepareConnection() throws SQLException {
                    return conn;
                }

                @Override
                public <U> U transaction(Transaction<U> tx, int isolation) {
                    throw new UnsupportedOperationException("already in transaction");
                }
            };
            T result = tx.apply(runner);
            conn.commit();
            commitSucc = true;
            return result;
        } finally {
            if (!commitSucc)
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            if (oldIsolation != -1)
                try {
                    conn.setTransactionIsolation(oldIsolation);
                } catch (SQLException ignored) {
                }
            DbUtils.closeQuietly(conn);
        }
    }

    /* ------------------------- transaction helper ------------------------- */

    /**
     * 保证在事务中执行。如当前不在事务中，则新起事务
     */
    public void runInTransaction(TransactionNoRet tx) throws Exception {
        if (inTransaction) {
            tx.apply(this);
        } else {
            transaction(tx);
        }
    }

    /**
     * 保证在事务中执行。如当前不在事务中，则新起事务
     */
    public <T> T runInTransaction(Transaction<T> tx) throws Exception {
        if (inTransaction) {
            return tx.apply(this);
        } else {
            return transaction(tx);
        }
    }


    /* ------------------------- with raw conn (no connection closing) ------------------------- */

    public static DBRunner withTransaction(final Connection conn) throws SQLException {
        boolean autoCommit = conn.getAutoCommit();
        if (autoCommit)
            throw new IllegalStateException("not in transaction");

        return new DBRunner("raw-conn", null, true) {
            @Override
            protected Connection prepareConnection() throws SQLException {
                return conn;
            }

            @Override
            public <T> T transaction(Transaction<T> tx, int isolation) {
                throw new UnsupportedOperationException("already in transaction");
            }
        };
    }


    public static DBRunner ofPro() {

        return DBRunner.of("pro", "jdbc:oracle:thin:@10.16.26.60:1521:svdp", "HLASSET", "GTIfZ8RnBY");//10.16.36.61  10.16.26.60
    }

    public static DBRunner ofVrf() {

        return DBRunner.of("vrf", "jdbc:oracle:thin:@10.16.36.61:1521:svdp", "HLASSET", "oracle");//10.16.36.61  10.16.26.60
    }
}
