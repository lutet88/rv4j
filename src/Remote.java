import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public abstract class Remote {
    Integer forcedHashCode;
    RemoteConnection rc;

    protected Remote(RemoteConnection rc, int forcedHashCode) {
        this.forcedHashCode = forcedHashCode;
        this.rc = rc;
    }

    protected Remote(RemoteConnection rc) {
        this.rc = rc;
    }

    public abstract boolean initialize(RemoteConnection rc);
    public abstract boolean delete();
    public abstract int idCode();

    public String getMainType() {
        return null;
    }

    protected void setHash(int hash) {
        this.forcedHashCode = hash;
    }

    protected void setRc(RemoteConnection rc) {
        this.rc = rc;
    }

    abstract String getClassName();

    protected ResultSet selectById(String table) {
        return this.rc.executeQuery("SELECT * FROM "+table+" WHERE id = " + idCode()+";");
    }

    protected static ResultSet select(RemoteConnection rc, String table) {
        return rc.executeQuery("SELECT * FROM "+table+";");
    }

    protected static void deleteTable(RemoteConnection rc, String table) {
        try {
            PreparedStatement stm = rc.getConnection().prepareStatement("DELETE FROM "+table);
            stm.executeUpdate();
        } catch (SQLException ignored) {}
    }

    protected static boolean delete(RemoteConnection rc, String table, int id) {
        try {
            PreparedStatement stm = rc.getConnection().prepareStatement("DELETE FROM "+table+" WHERE id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Set<? extends Remote> loadAll(RemoteConnection rc) {
        return null;
    }

    public static void deleteAll(RemoteConnection rc) {}

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Remote)) return false;
        return ((Remote) other).idCode() == this.idCode();
    }
}
