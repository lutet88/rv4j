import java.sql.*;
import java.util.*;

public class RemoteInteger extends RemoteSingleValue implements Comparable<RemoteInteger> {
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
        super(rc);
        initialize(rc);
        setValue(value);
        insertSingleValue(className, Integer.toString(hashCode()), Integer.toString(value));
    }

    private RemoteInteger (RemoteConnection rc, Integer forcedHash, boolean dummy) {
        super(rc, forcedHash);
    }

    public Integer getValue() {
        try {
            ResultSet rs = selectById(className);
            return rs.getInt("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(Integer value) {
        return updateSingleValue(className, value.toString());
    }

    public static Set<RemoteInteger> loadAll(RemoteConnection rc) {
        Set<RemoteInteger> s = new HashSet<>();
        try {
            rc.initialize("RemoteInteger", new String[]{"value"}, new String[]{"integer"});
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                s.add(new RemoteInteger(rc, rs.getInt("id"), true));
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
    public int compareTo(RemoteInteger other) {
        return getValue().compareTo(other.getValue());
    }
}
