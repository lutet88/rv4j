package rv4j;

import java.sql.*;

public class SQLiteConnection extends RemoteConnection{
    // conn is a java.sql.Connection, which is a SQL connection over jdbc drivers
    private Connection conn = null;

    // constructor, using the path to database
    public SQLiteConnection(String path) {
        if (!connect(path)) throw new RuntimeException("SQLite Connection not initialized");
    }

    // define conn. is called in constructor, for sanity's sake, but is public in case we close() and re-connect().
    @Override
    public boolean connect(String path) {
        if (conn != null) return true;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+path);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // close the connection, then set the conn to null.
    @Override
    public boolean close() {
        if (conn == null) return true;
        try {
            conn.close();
            conn = null;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // execute a SQL query as a string, returns success
    @Override
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }

    // execute a SQL update as a string, returns success
    @Override
    public boolean executeUpdate(String update) {
        try {
            Statement statement = conn.createStatement();
            return (statement.executeUpdate(update) != 0);
        } catch (SQLException e) {
            return false;
        }
    }

    // if conn is not null, the database should be connected, as this is SQLite
    @Override
    public boolean isConnected() {
        return conn != null;
    }

    // in theory conn == null and conn.isClosed() should always return the same result, but
    // when you delete the database and the java program's still running it closes the conn.
    @Override
    public boolean isClosed() throws SQLException {
        return conn == null || conn.isClosed();
    }

    // initialize a table, using typeName as table name, keys and types as values + id.
    @Override
    public void initialize(String typeName, String[] keys, String[] types) throws SQLException {
        Statement s = conn.createStatement();
        // 1. CREATE DATABASE
        // IGNORED BECAUSE SQLITE

        // 2. CREATE TABLE
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists \""+typeName+"\" (id integer");
        String prefix = ", ";
        for (int i = 0; i < keys.length; i++) {
            sb.append(prefix);
            sb.append(keys[i]);
            sb.append(" ");
            sb.append(types[i]);
        }
        sb.append(");");
        s.executeUpdate(sb.toString());
    }

    // get the inner connector for safe SQL queries.
    @Override
    public Connection getConnection() {
        return conn;
    }
}
