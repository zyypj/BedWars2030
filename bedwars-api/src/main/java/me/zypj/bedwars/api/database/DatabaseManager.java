package me.zypj.bedwars.api.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseManager {
    Connection getConnection() throws SQLException;

    void close() throws Exception;
}
