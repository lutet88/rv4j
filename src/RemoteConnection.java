import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.*;

public abstract class RemoteConnection {
    public abstract boolean connect(String path);
    public abstract boolean close();
    public abstract ResultSet executeQuery(String query);
    public abstract boolean executeUpdate(String update);
    public abstract boolean isConnected();
    public abstract boolean isClosed() throws SQLException;
    public abstract void initialize(String typeName, String[] keys, String[] types) throws SQLException;
    public abstract Connection getConnection();
}
