package rv4j;

import rv4j.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class RemoteSingleValue extends Remote {
    /*
    This is a helper class that allows easy implementation of any Remote object with a single value.
     */

    // constructors, simply to connect with super()'s constructors
    protected RemoteSingleValue(RemoteConnection rc, int forcedHashCode) {
        super(rc, forcedHashCode);
    }

    protected RemoteSingleValue(RemoteConnection rc) {
        super(rc);
    }

    // override initialize, with single value init on rc
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

    // given a table (className), hashCode, and value, insert it into the database
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

    // given a table and a value, update the value corresponding to this object's idCode()
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

    // for most Remote variables, idCode is simply the forced code if it is forced, otherwise the memory hashCode()
    @Override
    public int idCode() {
        if (forcedHashCode != null) return forcedHashCode;
        return hashCode();
    }

    // the getValue, which must be overriden by child classes
    public abstract Object getValue();

    @Override
    public String toString() {
        return getValue().toString();
    }
}
