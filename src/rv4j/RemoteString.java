package rv4j;

import rv4j.*;

import java.sql.*;
import java.util.*;

public class RemoteString extends RemoteSingleValue implements Comparable<RemoteString> {
    private static final String className = "RemoteString";
    private static final String mainType = "varbinary(32768)";

    public static String getType() { return mainType; }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public RemoteString (RemoteConnection rc, String value) {
        super(rc);
        initialize(rc);
        setValue(value);
        insertSingleValue(className, Integer.toString(hashCode()), "'"+value+"'");
    }

    RemoteString (RemoteConnection rc, Integer forcedHash) {
        super(rc, forcedHash);
    }

    public String getValue() {
        try {
            ResultSet rs = selectById(className);
            return rs.getString("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(String value) {
        return updateSingleValue(className, "'"+value+"'");
    }

    public static Set<RemoteString> loadAll(RemoteConnection rc) {
        Set<RemoteString> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                s.add(new RemoteString(rc, rs.getInt("id")));
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
    public int compareTo(RemoteString other) {
        return getValue().compareTo(other.getValue());
    }
}
