package me.zypj.bedwars.common.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zypj.bedwars.common.enums.DatabaseType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfig {
    private DatabaseType type;
    private MySQLConfig  mysql;
}
