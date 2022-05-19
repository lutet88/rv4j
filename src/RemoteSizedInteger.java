import java.sql.*;
import java.util.*;

public class RemoteSizedInteger extends RemoteNumerical {
    private static String className;
    private static String mainType;
    private int bitSize;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public RemoteSizedInteger (RemoteConnection rc, Long value, int bitSize) {
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");
        this.bitSize = bitSize;
        className = "RemoteSizedInteger" + bitSize;
        mainType = "bit("+bitSize+")";
        initialize(rc);
        setValue(value);
        this.rc.executeUpdate("INSERT INTO "+getClassName()+" (id, value) VALUES ("+hashCode() + ", "+value+");");
    }

    private RemoteSizedInteger (RemoteConnection rc, Integer forcedHash, int bitSize) {
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");
        this.bitSize = bitSize;
        className = "RemoteSizedInteger" + bitSize;
        mainType = "bit("+bitSize+")";
        this.rc = rc;
        forcedHashCode = forcedHash;
    }

    public Long getValue() {
        try {
            ResultSet rs = this.rc.executeQuery("SELECT value FROM "+className+" WHERE id = " + idCode());
            return rs.getLong("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public static Set<RemoteSizedInteger> loadAll(RemoteConnection rc, int bitSize) {
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");

        Set<RemoteSizedInteger> s = new HashSet<>();
        try {
            rc.initialize("RemoteSizedInteger"+bitSize, new String[]{"value"}, new String[]{"bit("+bitSize+")"});
            ResultSet rs = rc.executeQuery("SELECT * FROM RemoteInteger;");
            while (rs.next()) {
                s.add(new RemoteSizedInteger(rc, rs.getInt("id"), bitSize));
            }
            return s;
        } catch (SQLException e) {
            return null;
        }
    }
}
