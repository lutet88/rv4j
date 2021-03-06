package rv4j;

import rv4j.*;

import java.sql.*;
import java.util.*;

public class RemoteDouble extends RemoteSingleValue implements Comparable<RemoteDouble> {
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

    public static String getType() { return mainType; }

    public RemoteDouble (RemoteConnection rc, Double value) {
        super(rc);
        initialize(rc);
        setValue(value);
        insertSingleValue(className, Integer.toString(hashCode()), Double.toString(value));
    }

    RemoteDouble (RemoteConnection rc, Integer forcedHash) {
        super(rc, forcedHash);
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
        return updateSingleValue(className, value.toString());
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

    @Override
    public int compareTo(RemoteDouble other) {
        return getValue().compareTo(other.getValue());
    }
}
