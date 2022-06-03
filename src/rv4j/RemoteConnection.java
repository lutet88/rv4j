package rv4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RemoteConnection {
    /*
    This is the base class for a connection to a remote database.
    See SQLiteConnection for a concrete implementation.
    This is also an abstract class, not an interface, because previously there were a few instance variables.
    I believe these variables may be necessary in the future, if this package is expanded upon.
     */

    public abstract boolean connect(String path);
    public abstract boolean close();
    public abstract ResultSet executeQuery(String query);
    public abstract boolean executeUpdate(String update);
    public abstract boolean isConnected();
    public abstract boolean isClosed() throws SQLException;
    public abstract void initialize(String typeName, String[] keys, String[] types) throws SQLException;
    public abstract Connection getConnection();
}
