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
        insert(className, Integer.toString(hashCode()), Double.toString(value));
    }

    private RemoteDouble (RemoteConnection rc, Integer forcedHash) {
        this.rc = rc;
        forcedHashCode = forcedHash;
    }

    public Double getValue() {
        try {
            ResultSet rs = selectById(className);
            return rs.getDouble("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(Double value) {
        return updateValue(className, value.toString());
    }

    public static Set<RemoteDouble> loadAll(RemoteConnection rc) {
        Set<RemoteDouble> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                s.add(new RemoteDouble(rc, rs.getInt("id")));
            }
            return s;
        } catch (SQLException e) {
            return null;
        } catch (NullPointerException e) {
            return s;
        }
    }

    public static void deleteAll(RemoteConnection rc) {
        deleteTable(rc, className);
    }
}
