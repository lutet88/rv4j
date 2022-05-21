import java.sql.SQLException;

public abstract class RemoteSingleValue extends Remote {
    abstract String getClassName();
    abstract String getMainType();
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
        return this.rc.executeUpdate("INSERT INTO "+className+" (id, value) VALUES ("+hashCode + ", "+value+");");
    }

    protected boolean updateSingleValue(String className, String value) {
        return this.rc.executeUpdate("UPDATE " + className + " SET value = " + value + " WHERE id = " + idCode() + ";");
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
