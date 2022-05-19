import java.sql.*;
import java.util.*;

public class RemoteFloat extends RemoteNumerical {

    private static final String className = "RemoteFloat";
    private static final String mainType = "float(24)";

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public RemoteFloat (RemoteConnection rc, Float value) {
        initialize(rc);
        setValue(value);
        this.rc.executeUpdate("INSERT INTO "+getClassName()+" (id, value) VALUES ("+hashCode() + ", "+value+");");
    }

    private RemoteFloat (RemoteConnection rc, Integer forcedHash) {
        this.rc = rc;
        forcedHashCode = forcedHash;
    }

    public Float getValue() {
        try {
            ResultSet rs = this.rc.executeQuery("SELECT value FROM "+getClassName()+" WHERE id = " + idCode());
            return rs.getFloat("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public static Set<RemoteFloat> loadAll(RemoteConnection rc) {
        Set<RemoteFloat> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = rc.executeQuery("SELECT * FROM "+className+";");
            while (rs.next()) {
                s.add(new RemoteFloat(rc, rs.getInt("id")));
            }
            return s;
        } catch (SQLException e) {
            return null;
        }
    }
}
