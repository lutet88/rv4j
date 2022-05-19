import java.sql.*;
import java.util.*;

public class RemoteInteger extends Remote {
    private RemoteConnection rc;
    private Integer forcedHashCode;

    public RemoteInteger (RemoteConnection rc, Integer value) {
        initialize(rc);
        setValue(value);
        this.rc.executeUpdate("INSERT INTO RemoteInteger (id, value) VALUES ("+hashCode() + ", "+value+");");
    }

    private RemoteInteger (RemoteConnection rc, Integer forcedHash, boolean dummy) {
        this.rc = rc;
        this.forcedHashCode = forcedHash;
    }

    @Override
    public boolean initialize(RemoteConnection rc) {
        try {
            this.rc = rc;
            this.rc.initialize("RemoteInteger", new String[]{"value"}, new String[]{"integer"});
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean setValue(Integer value) {
        return this.rc.executeUpdate("UPDATE RemoteInteger SET value = " + value.toString() + " WHERE id = " + idCode() + ";");
    }

    @Override
    public boolean delete() {
        return this.rc.executeUpdate("DELETE FROM RemoteInteger WHERE id = "+ idCode() + ";");
    }

    @Override
    public int idCode() {
        if (forcedHashCode != null) return forcedHashCode;
        return hashCode();
    }

    public Integer getValue() {
        try {
            ResultSet rs = this.rc.executeQuery("SELECT value FROM RemoteInteger WHERE id = " + idCode());
            return rs.getInt("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public static Set<RemoteInteger> loadAll(RemoteConnection rc) {
        ResultSet rs = rc.executeQuery("SELECT * FROM RemoteInteger;");
        Set<RemoteInteger> s = new HashSet<>();
        try {
            while (rs.next()) {
                s.add(new RemoteInteger(rc, rs.getInt("id"), true));
            }
            return s;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
