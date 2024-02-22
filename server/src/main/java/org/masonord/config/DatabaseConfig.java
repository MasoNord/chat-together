package org.masonord.config;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static final PropertyConfig config = new PropertyConfig("application.properties");

    public static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(Integer.parseInt(config.getProperty("pool.size")));
        ds.setJdbcUrl(config.getProperty("url.db"));
        ds.setUsername(config.getProperty("username.db"));
        ds.setPassword(config.getProperty("password.db"));
        return ds;
    }

}
