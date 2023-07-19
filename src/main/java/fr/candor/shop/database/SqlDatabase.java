package fr.candor.shop.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.candor.shop.ShopConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SqlDatabase {
    private final HikariDataSource source;

    public SqlDatabase(ShopConfig.SqlSection sql) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", sql.getHost(), sql.getPort(), sql.getDatabase()));
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(sql.getUser());
        config.setPassword(sql.getPassword());
        config.setMaximumPoolSize(sql.getPoolSize());
        config.setConnectionTimeout(30_000);
        this.source = new HikariDataSource(config);
    }

    protected Connection getConnection() {
        try {
            return this.source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get SQL connection", e);
        }
    }

    protected final void createClosingStatement(SqlStatementExecutor<Statement> consumer) {
        try (final Connection connection = this.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                consumer.execute(statement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected final <T> T createResultStatement(SqlResultExecutor<T, Statement> consumer) {
        try (final Connection connection = this.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                return consumer.execute(statement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void prepareClosingStatement(String preparedStatement, SqlStatementExecutor<PreparedStatement> consumer) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(preparedStatement)) {
                consumer.execute(statement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
