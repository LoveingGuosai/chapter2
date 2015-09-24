package org.smart4j.chapter2.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.PropsUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作助手
 */
public final class DatabaseHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = PropsUtil.getString(properties, "jdbc.driver");
        URL = PropsUtil.getString(properties, "jdbc.url");
        USERNAME = PropsUtil.getString(properties, "jdbc.username");
        PASSWORD = PropsUtil.getString(properties, "jdbc.password");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库链接
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    /**
     * 关闭数据库链接
     */
    public static void closeConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        Connection conn = getConnection();
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 查询单个实体
     */
    public static <T> T queryEntity(Class<T> entityClass, Object... params) {
        T entity;
        Connection conn = getConnection();
        String sql="SELECT * FROM "+getTableName(entityClass)+" WHERE ID=?";
        try {
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entity;
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
            result = QUERY_RUNNER.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("executy query failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * 执行更新瑜伽(包括 update insert delete)
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            rows = QUERY_RUNNER.update(getConnection(), sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return rows;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity : fieldMap is empty");
            return false;
        }
        String sql = "UPDATE "+getTableName(entityClass)+" SET ";
        StringBuilder columns=new StringBuilder();
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldMap).append("=?, ");
        }
        sql+=columns.substring(0,columns.lastIndexOf(", "))+" WHERE ID=?";
        List<Object> paramList= new ArrayList<>();
        paramList.add(fieldMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     *删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "DELETE FROM "+getTableName(entityClass)+"WHERE ID=?";
        return executeUpdate(sql,id)==1;
    }


    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
}
