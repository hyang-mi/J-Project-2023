package com.mjuAppSW.joA.log;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryDev {
    private static interface Singleton {
        final ConnectionFactoryDev INSTANCE = new ConnectionFactoryDev();
    }

    private final DataSource dataSource;

    private ConnectionFactoryDev() {
        Properties properties = new Properties();
        properties.setProperty("user", "dbmasteruser");
        properties.setProperty("password", ")zY+0rhx%vw}Fhm$_#1d9%(b,vPx$*Ca");

        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                "jdbc:mysql://ls-18bf6de031311b3e933799ef9735005aeb6b6f74.c16t0ksf3vij.ap-northeast-2.rds.amazonaws.com:3306/database-jproject-1", properties
        );
        new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);

        this.dataSource = new PoolingDataSource(pool);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}
