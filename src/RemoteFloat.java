import java.sql.*;
import java.util.*;

public class RemoteFloat extends RemoteSingleValue implements Comparable<RemoteFloat> {

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

    public static String getType() { return mainType; }

    public RemoteFloat (RemoteConnection rc, Float value) {
        super(rc);
        initialize(rc);
        setValue(value);
        insertSingleValue(className, Integer.toString(hashCode()), Float.toString(value));
    }

    RemoteFloat (RemoteConnection rc, Integer forcedHash) {
        super(rc, forcedHash);
    }

    public Float getValue() {
        try {
            ResultSet rs = selectById(className);
            return rs.getFloat("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(Float value) {
        return updateSingleValue(className, value.toString());
    }

    public static Set<RemoteFloat> loadAll(RemoteConnection rc) {
        Set<RemoteFloat> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                s.add(new RemoteFloat(rc, rs.getInt("id")));
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
    public int compareTo(RemoteFloat other) {
        return getValue().compareTo(other.getValue());
    }
}
