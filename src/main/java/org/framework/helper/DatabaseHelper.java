package org.framework.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import org.framework.util.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.*;

/**
 * 数据库助手类
 */
public final class DatabaseHelper {
    private static final Logger                  LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner             QUERY_RUNNER;
    private static final BasicDataSource         DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER      = new QueryRunner();
        DATA_SOURCE       = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUserName());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }


    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();

        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);

                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }

        return conn;
    }

    /**
     * 执行查询语句
     *
     * @param sql
     * @param params
     *
     * @return
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;

        try {
            Connection conn = getConnection();

            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);

            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     *
     * @param sql
     * @param params
     *
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;

        try {
            Connection conn = getConnection();

            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);

            throw new RuntimeException(e);
        }

        return rows;
    }

    /**
     * 查询实体列表
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;

        try {
            Connection conn = getConnection();

            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);

            throw new RuntimeException(e);
        }

        return entityList;
    }

    /**
     * 查询实体
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;

        try {
            Connection conn = getConnection();

            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);

            throw new RuntimeException(e);
        }

        return entity;
    }

    /**
     * 插入实体
     *
     * @param entityClass
     * @param fieldMap
     * @param <T>
     *
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");

            return false;
        }

        String        sql     = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values  = new StringBuilder("(");

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     *
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     *
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");

            return false;
        }

        String        sql     = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }

        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();

        paramList.addAll(fieldMap.values());
        paramList.add(id);

        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     *
     * @param entityClass
     * @param id
     * @param <T>
     *
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 执行 SQL 文件
     *
     * @param filePath
     */
    public static void executeSqlFile(String filePath) {
        InputStream    is     = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String sql;

            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (Exception e) {
            LOGGER.error("execute sql file failure", e);

            throw new RuntimeException(e);
        }
    }

    /**
     * author：Lizhao
     * Date:15/12/21
     * version:1.0
     *
     * @param entityClass
     *
     * @return
     */
    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * 开启事务
     * author：Lizhao
     * Date:15/12/21
     * version:1.0
     */
    public static void beginTranstion() {
        Connection conn = getConnection();

        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure", e);

                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事务
     * author：Lizhao
     * Date:15/12/21
     * version:1.0
     */
    public static void commitTransaction() {
        Connection conn = getConnection();

        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure", e);

                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();

        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("roolback transaction failure", e);

                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询并返回单个列的值
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param sql
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> T query(String sql, Object... params) {
        T obj;

        try {
            Connection conn = getConnection();

            obj = QUERY_RUNNER.query(conn, sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
            LOGGER.error("query failure", e);

            throw new RuntimeException(e);
        }

        return obj;
    }

    /**
     * 查询返回多个列值且唯一性，用Set不用distinct
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param sql
     * @param params
     * @param <T>
     *
     * @return
     */
    public static <T> Set<T> querySet(String sql, Object... params) {
        Collection<T> valueList = queryList(sql, params);

        return new LinkedHashSet<T>(valueList);
    }

    /**
     * 查询并返回多个列值
     * author：Lizhao
     * Date:16/01/06
     * version:1.0
     *
     * @param sql
     * @param params
     * @param <T>
     *
     * @return
     */
    private static <T> Collection<T> queryList(String sql, Object... params) {
        List<T> list;

        try {
            Connection conn = getConnection();

            list = QUERY_RUNNER.query(conn, sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
            LOGGER.error("query list failure", e);

            throw new RuntimeException(e);
        }

        return list;
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
