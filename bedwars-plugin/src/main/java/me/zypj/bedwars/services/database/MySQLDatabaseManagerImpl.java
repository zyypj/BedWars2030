package me.zypj.bedwars.services.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.common.model.config.MySQLConfig;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class MySQLDatabaseManagerImpl implements DatabaseManager {

    private final HikariDataSource dataSource;

    public MySQLDatabaseManagerImpl(MySQLConfig config) {
        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase() + "?useSSL=false");
        hc.setUsername(config.getUsername());
        hc.setPassword(config.getPassword());
        hc.setMaximumPoolSize(10);
        hc.setMinimumIdle(2);
        hc.setConnectionTimeout(30_000);
        this.dataSource = new HikariDataSource(hc);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
