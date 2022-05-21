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
        insert(className, Integer.toString(hashCode()), Float.toString(value));
    }

    private RemoteFloat (RemoteConnection rc, Integer forcedHash) {
        this.rc = rc;
        forcedHashCode = forcedHash;
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
        return updateValue(className, value.toString());
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
}
