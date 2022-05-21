import java.sql.*;
import java.util.*;

public class RemoteSizedInteger extends RemoteNumerical {
    private String className;
    private String mainType;
    private int bitSize;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    public int getBitSize() {
        return bitSize;
    }

    public RemoteSizedInteger (RemoteConnection rc, Long value, int bitSize) {
        if (bitSize < 1)  throw new RuntimeException("BitSize invalid for RemoteSizedInteger!");
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");
        this.bitSize = bitSize;
        className = "RemoteSizedInteger" + bitSize;
        mainType = "bit("+bitSize+")";
        initialize(rc);
        insert(className, Integer.toString(hashCode()), Long.toString(value));
        setValue(value);
    }

    private RemoteSizedInteger (RemoteConnection rc, Integer forcedHash, int bitSize) {
        if (bitSize < 1)  throw new RuntimeException("BitSize invalid for RemoteSizedInteger!");
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");
        this.bitSize = bitSize;
        className = "RemoteSizedInteger" + bitSize;
        mainType = "bit("+bitSize+")";
        this.rc = rc;
        forcedHashCode = forcedHash;
    }

    public Long getValue() {
        try {
            ResultSet rs = selectById(className);
            return rs.getLong("value");
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean setValue(Long value) {
        Long v;
        if (bitSize != 64) {
            v = value & ((long) Math.pow(2, bitSize) - 1);
        } else {
            v = value;
        }
        return updateValue(className, v.toString());
    }

    public static Set<RemoteSizedInteger> loadAll(RemoteConnection rc, int bitSize) {
        if (bitSize > 64) throw new RuntimeException("BitSize too large for RemoteSizedInteger!");

        Set<RemoteSizedInteger> s = new HashSet<>();
        try {
            rc.initialize("RemoteSizedInteger"+bitSize, new String[]{"value"}, new String[]{"bit("+bitSize+")"});
            ResultSet rs = select(rc, "RemoteSizedInteger"+bitSize);
            while (rs.next()) {
                s.add(new RemoteSizedInteger(rc, rs.getInt("id"), bitSize));
            }
            return s;
        } catch (SQLException e) {
            return null;
        } catch (NullPointerException e) {
            return s;
        }
    }

    public static void deleteAll(RemoteConnection rc, int bitSize) {
        deleteTable(rc, "RemoteSizedInteger"+bitSize);
    }

    public static void deleteAll(RemoteConnection rc) {
        for (int bitSize = 1; bitSize <= 64; bitSize++)
            deleteTable(rc, "RemoteSizedInteger"+bitSize);
    }
}
