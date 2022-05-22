import java.sql.*;

public class SQLiteConnection extends RemoteConnection{
    private Connection conn = null;

    public SQLiteConnection(String path) {
        if (!connect(path)) throw new RuntimeException("SQLite Connection not initialized");
    }

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

    @Override
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean executeUpdate(String update) {
        try {
            Statement statement = conn.createStatement();
            return (statement.executeUpdate(update) != 0);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean isConnected() {
        return conn != null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return conn == null || conn.isClosed();
    }

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

    @Override
    public Connection getConnection() {
        return conn;
    }
}
