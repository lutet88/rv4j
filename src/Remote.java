import java.sql.ResultSet;
import java.util.Set;

public abstract class Remote {
    Integer forcedHashCode;
    RemoteConnection rc;

    public abstract boolean initialize(RemoteConnection rc);
    public abstract boolean delete();
    public abstract int idCode();

    protected ResultSet selectById(String table) {
        return this.rc.executeQuery("SELECT * FROM "+table+" WHERE id = " + idCode()+";");
    }

    protected static ResultSet select(RemoteConnection rc, String table) {
        return rc.executeQuery("SELECT * FROM "+table+";");
    }

    protected static void deleteTable(RemoteConnection rc, String table) {
        rc.executeUpdate("DELETE FROM " + table + ";");
    }

    public static Set<? extends Remote> loadAll(RemoteConnection rc) {
        return null;
    }

    public static void deleteAll(RemoteConnection rc) {}

}
