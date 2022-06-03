package rv4j;

import rv4j.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class RemoteSingleValue extends Remote {

    protected RemoteSingleValue(RemoteConnection rc, int forcedHashCode) {
        super(rc, forcedHashCode);
    }

    protected RemoteSingleValue(RemoteConnection rc) {
        super(rc);
    }

    @Override
    public boolean initialize(RemoteConnection rc) {
        try {
            this.rc = rc;
            this.rc.initialize(getClassName(), new String[]{"value"}, new String[]{getMainType()});
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    protected boolean insertSingleValue(String className, String hashCode, String value) {
        try {
            PreparedStatement stm = rc.getConnection().prepareStatement("INSERT INTO "+className+" (id, value) VALUES ("+hashCode + ", ? );");
            stm.setString(1, value);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    protected boolean updateSingleValue(String className, String value) {
        try {
            PreparedStatement stm = rc.getConnection().prepareStatement("UPDATE " + className + " SET value = ? WHERE id = " + idCode() + ";");
            stm.setString(1, value);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete() {
        return delete(rc, getClassName(), idCode());
    }

    @Override
    public int idCode() {
        if (forcedHashCode != null) return forcedHashCode;
        return hashCode();
    }

    public abstract Object getValue();

    @Override
    public String toString() {
        return getValue().toString();
    }
}
