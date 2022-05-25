import java.sql.*;
import java.util.*;

public class RemoteCharacter extends RemoteSingleValue implements Comparable<RemoteCharacter> {
    private static final String className = "RemoteCharacter";
    private static final String mainType = "char(1)";

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public static String getType() { return mainType; }

    public RemoteCharacter (RemoteConnection rc, Character value) {
        super(rc);
        initialize(rc);
        setValue(value);
        insertSingleValue(className, Integer.toString(hashCode()), Double.toString(value));
    }

    RemoteCharacter (RemoteConnection rc, Integer forcedHash) {
        super(rc, forcedHash);
    }

    public Character getValue() {
        try {
            ResultSet rs = selectById(className);
            return (char) rs.getInt("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(Character value) {
        return updateSingleValue(className, Integer.valueOf(value).toString());
    }

    public static Set<RemoteCharacter> loadAll(RemoteConnection rc) {
        Set<RemoteCharacter> s = new HashSet<>();
        try {
            rc.initialize(className, new String[]{"value"}, new String[]{mainType});
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                s.add(new RemoteCharacter(rc, rs.getInt("id")));
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
    public int compareTo(RemoteCharacter other) {
        return getValue().compareTo(other.getValue());
    }
}
