package fr.candor.shop;

import fr.bramsou.yaml.api.YamlAPI;
import fr.bramsou.yaml.api.configuration.ConfigurationType;
import fr.bramsou.yaml.api.configuration.YamlConfiguration;
import fr.bramsou.yaml.api.configuration.dynamic.ConfigurationPart;
import fr.bramsou.yaml.api.configuration.dynamic.annotation.ConfigurationPath;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ShopConfig {

    private final YamlConfiguration configuration;

    @ConfigurationPath(value = "sql", comments = "MySQL credentials")
    private final SqlSection sql = new SqlSection();

    public ShopConfig(JavaPlugin plugin) {
        this.configuration = YamlAPI.INSTANCE.getConfigurationManager().loadConfiguration(this, new File(plugin.getDataFolder(), "shop.yml"), ConfigurationType.DYNAMIC);
        this.load();
    }

    public void load() {
        this.configuration.load();
        this.configuration.save();
    }

    public SqlSection getSql() {
        return sql;
    }

    static class SqlSection extends ConfigurationPart {
        @ConfigurationPath("host")
        private final String host = "localhost";
        @ConfigurationPath("port")
        private final int port = 3306;
        @ConfigurationPath("user")
        private final String user = "root";
        @ConfigurationPath("password")
        private final String password = "root";
        @ConfigurationPath("database")
        private final String database = "candor-shop";
        @ConfigurationPath("pool-size")
        private final int poolSize = 5;

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getDatabase() {
            return database;
        }

        public int getPoolSize() {
            return poolSize;
        }
    }
}
