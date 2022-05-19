import java.sql.*;
import java.util.*;

public class RemoteDouble extends RemoteNumerical {
    private static final String className = "RemoteDouble";
    private static final String mainType = "float(53)";

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public RemoteDouble (RemoteConnection rc, Double value) {
        initialize(rc);
        setValue(value);
        this.rc.executeUpdate("INSERT INTO "+className+" (id, value) VALUES ("+hashCode() + ", "+value+");");
    }

    private RemoteDouble (RemoteConnection rc, Integer forcedHash) {
        this.rc = rc;
        forcedHashCode = forcedHash;
    }

    public Double getValue() {
        try {
            ResultSet rs = this.rc.executeQuery("SELECT value FROM "+className+" WHERE id = " + idCode());
            return rs.getDouble("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public static Set<RemoteDouble> loadAll(RemoteConnection rc) {
        Set<RemoteDouble> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = rc.executeQuery("SELECT * FROM "+className+";");
            while (rs.next()) {
                s.add(new RemoteDouble(rc, rs.getInt("id")));
            }
            return s;
        } catch (SQLException e) {
            return null;
        }
    }
}
