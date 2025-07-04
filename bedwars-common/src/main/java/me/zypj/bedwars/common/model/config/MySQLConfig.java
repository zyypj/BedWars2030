package me.zypj.bedwars.common.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySQLConfig {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
}
