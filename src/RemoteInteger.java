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
        insert(className, Integer.toString(hashCode()), Integer.toString(value));
    }

    private RemoteInteger (RemoteConnection rc, Integer forcedHash, boolean dummy) {
        this.rc = rc;
        forcedHashCode = forcedHash;
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
        return updateValue(className, value.toString());
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
}
