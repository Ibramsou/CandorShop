package fr.candor.shop.database;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface SqlStatementExecutor<T extends Statement> {

    void execute(T statement) throws SQLException;
}
