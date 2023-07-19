package fr.candor.shop.database;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface SqlResultExecutor<T, V extends Statement> {

    T execute(V statement) throws SQLException;
}
