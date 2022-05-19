import java.sql.*;
import java.util.*;

public class RemoteInteger extends RemoteNumerical {
    private static final String className = "RemoteInteger";
    private static final String mainType = "integer";

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public RemoteInteger (RemoteConnection rc, Integer value) {
        initialize(rc);
        setValue(value);
        this.rc.executeUpdate("INSERT INTO RemoteInteger (id, value) VALUES ("+hashCode() + ", "+value+");");
    }

    private RemoteInteger (RemoteConnection rc, Integer forcedHash, boolean dummy) {
        this.rc = rc;
        forcedHashCode = forcedHash;
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
        Set<RemoteInteger> s = new HashSet<>();
        try {
            rc.initialize("RemoteInteger", new String[]{"value"}, new String[]{"integer"});
            ResultSet rs = rc.executeQuery("SELECT * FROM RemoteInteger;");
            while (rs.next()) {
                s.add(new RemoteInteger(rc, rs.getInt("id"), true));
            }
            return s;
        } catch (SQLException e) {
            return null;
        }
    }
}
