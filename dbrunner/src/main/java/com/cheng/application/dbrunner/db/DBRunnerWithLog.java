package com.cheng.application.dbrunner.db;

import com.cheng.application.basic.Predef;
import com.cheng.application.dbrunner.DBRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.cheng.application.basic.Predef.f;


public abstract class DBRunnerWithLog extends QueryRunner {

    /* ------------------------- init ------------------------- */

    static final Logger logger = LoggerFactory.getLogger(DBRunner.class);
    public final String name;

    public DBRunnerWithLog(String name, DataSource ds, boolean closeConn) {
        super(ds, closeConn);
        this.name = name;

    }

    /* ------------------------- log ------------------------- */

    public static boolean trace = false;
    public static boolean trace_longtime = true;
    public static int op_longtime = 200;


    private void logQuery(long start, boolean isSucc, String sql, Object[] params) {
        int cost = (int) (System.currentTimeMillis() - start);
        boolean isLongtime = cost > op_longtime;

        if (trace)
            logger.info(Predef.f("query %s cost %s: %s", isSucc ? "ok" : "fail", cost, toStr(sql, params)));
        if (trace_longtime && isLongtime)
            logger.info(f("longtime query %s cost %s: %s", isSucc ? "ok" : "fail", cost, toStr(sql, params)));
    }

    private void logUpdate(long start, boolean isSucc, String sql, Object[] params) {
        int cost = (int) (System.currentTimeMillis() - start);
        boolean isLongtime = cost > op_longtime;

        if (trace)
            logger.info(f("update %s cost %s: %s", isSucc ? "ok" : "fail", cost, toStr(sql, params)));
        if (trace_longtime && isLongtime)
            logger.info(f("longtime update %s cost %s: %s", isSucc ? "ok" : "fail", cost, toStr(sql, params)));

    }

    private static String toStr(String sql, Object[] params) {
        if (params == null || params.length <= 0) return sql;
        return sql + " (" + StringUtils.join(params, ", ") + ")";
    }

    /* ------------------------- overrides (按QueryRunner中排序) ------------------------- */

    @Override
    public final int[] batch(String sql, Object[][] params) throws SQLException {
        long start = System.currentTimeMillis();
        boolean isSucc = true;
        try {
            return super.batch(sql, params);
        } catch (SQLException e) {
            isSucc = false;
            throw e;
        } finally {
            logUpdate(start, isSucc, sql, null);
        }
    }

    @Override
    public final <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        long start = System.currentTimeMillis();
        boolean isSucc = true;
        try {
            return super.query(sql, rsh, params);
        } catch (SQLException e) {
            isSucc = false;
            throw e;
        } finally {
//			logQuery(start, isSucc, sql, params);
        }
    }

    @Override
    public final int update(String sql, Object... params) throws SQLException {
        long start = System.currentTimeMillis();
        boolean isSucc = true;
        try {
            return super.update(sql, params);
        } catch (SQLException e) {
            isSucc = false;
            throw e;
        } finally {
            logUpdate(start, isSucc, sql, params);
        }
    }

    @Override
    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        long start = System.currentTimeMillis();
        boolean isSucc = true;
        try {
            return super.insert(sql, rsh, params);
        } catch (SQLException e) {
            isSucc = false;
            throw e;
        } finally {
            logUpdate(start, isSucc, sql, params);
        }
    }

    @Override
    public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][] params) throws SQLException {
        long start = System.currentTimeMillis();
        boolean isSucc = true;
        try {
            return super.insertBatch(sql, rsh, params);
        } catch (SQLException e) {
            isSucc = false;
            throw e;
        } finally {
            logUpdate(start, isSucc, sql, null);
        }
    }

}
