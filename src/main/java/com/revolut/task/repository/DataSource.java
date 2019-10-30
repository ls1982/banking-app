package com.revolut.task.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides connection to database from connection pool
 *
 * @author Alexey Smirnov
 */
final class DataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class);
    private static HikariDataSource ds;

    static {
        try {
            final Properties properties = new Properties();
            properties.load(DataSource.class.getClassLoader().getResourceAsStream("datasource.properties"));

            final HikariConfig config = new HikariConfig(properties);
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private DataSource() {
        //not instantiable
    }

    /**
     * Gets a connection from connection pool
     *
     * @return {@link Connection} connection
     */
    static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
