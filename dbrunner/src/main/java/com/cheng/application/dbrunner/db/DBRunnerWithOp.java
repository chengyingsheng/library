package com.cheng.application.dbrunner.db;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.cheng.application.dbrunner.db.DBRunnerHandlers.*;


public class DBRunnerWithOp extends DBRunnerWithLog {

    /* ------------------------- init ------------------------- */

    public DBRunnerWithOp(String name, DataSource ds, boolean closeConn) {
        super(name, ds, closeConn);
    }

    /* ------------------------- shortcuts - single row ------------------------- */

    public int queryInt(String sql, Object... params) throws SQLException {
        return query(sql, singleIntHandler, params);
    }

    public long queryLong(String sql, Object... params) throws SQLException {
        return query(sql, singleLongHandler, params);
    }

    public double queryDouble(String sql, Object... params) throws SQLException {
        return query(sql, singleDoubleHandler, params);
    }

    public String queryString(String sql, Object... params) throws SQLException {
        return query(sql, singleStringHandler, params);
    }

    public Timestamp queryTimestamp(String sql, Object... params) throws SQLException {
        return query(sql, singleTimestampHandler, params);
    }

    private <T> T queryBean(Class<T> type, String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToBean.of(type), sql, params);
    }


    private String createQuerySql(String tablename, String[] selectfield, String condition) {
        return createQuerySql(tablename, selectfield, condition, null);
    }

    private String createQuerySql(String tablename, String[] selectfield, String condition, String orderbycondition) {
        StringBuffer stringBuffer = new StringBuffer("select");
        if (selectfield == null || selectfield.length == 0) {
            stringBuffer.append(" * ");
        } else {
            stringBuffer.append(" ");
            for (String pc : selectfield) {
                stringBuffer.append(pc).append(",");
            }
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        stringBuffer.append(" from ").append(tablename);
        if (StringUtils.isNotBlank(condition)) {
            stringBuffer.append(" where ").append(condition);
        }
        if (StringUtils.isNotBlank(orderbycondition)) {
            stringBuffer.append(" order by ").append(orderbycondition);
        }
        return stringBuffer.toString();
    }

    public Map<String, Object> queryMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToDbHelperMap.instance, sql, params);
    }

    public Map<String, Object> queryRawMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToRawMap.instance, sql, params);
    }

    public Map<String, String> queryStringMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToStringMap.instance, sql, params);
    }

    public <T> T queryOne(final RowMapper<T> mapper, String sql, Object... params) throws SQLException {
        return query(sql, new ResultSetHandler<T>() {
            public T handle(ResultSet rs) throws SQLException {
                return rs.next() ? mapper.apply(rs) : null;
            }
        }, params);
    }

    /* ------------------------- shortcuts - multi rows ------------------------- */

    public List<Integer> queryIntList(String sql, Object... params) throws SQLException {
        return query(sql, intListHandler, params);
    }

    public List<Long> queryLongList(String sql, Object... params) throws SQLException {
        return query(sql, longListHandler, params);
    }

    public List<String> queryStringList(String sql, Object... params) throws SQLException {
        return query(sql, stringListHandler, params);
    }

    private <T> List<T> queryBeans(Class<T> type, String sql, Object... params) throws SQLException {
        return queryList(RowMapperToBean.of(type), sql, params);
    }

    public List<Map<String, Object>> queryMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToDbHelperMap.instance, sql, params);
    }

    public List<Map<String, Object>> queryRawMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToRawMap.instance, sql, params);
    }

    public List<Map<String, String>> queryStringMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToStringMap.instance, sql, params);
    }

    public <T> List<T> queryList(final RowMapper<T> mapper, String sql, Object... params) throws SQLException {
        return query(sql, new AbstractListHandler<T>() {
            protected T handleRow(ResultSet rs) throws SQLException {
                return mapper.apply(rs);
            }
        }, params);
    }


    /* ------------------------- misc ------------------------- */

    public int insertReturnInt(String sql, Object... params) throws SQLException {
        return super.insert(sql, singleIntHandler, params);
    }


    public synchronized int dump(String sql, Object... params) throws SQLException {
        return query(sql, new ResultSetHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                ResultSetMetaData meta = rs.getMetaData();
                int len = meta.getColumnCount();
                PrintStream out = System.out;

                int rows = 0;
                while (rs.next()) {
                    // bell(2014-6): 无输出则不print thead
                    if (rows == 0) {
                        for (int i = 1; i <= len; i++) {
                            if (i > 1)
                                out.print(" \t ");
                            out.print(meta.getColumnLabel(i));
                        }
                        out.println();
                    }

                    rows++;

                    for (int i = 1; i <= len; i++) {
                        if (i > 1)
                            out.print(" \t ");
                        out.print(rs.getObject(i));
                    }
                    out.println();
                }
                return rows;
            }
        }, params);
    }
}
