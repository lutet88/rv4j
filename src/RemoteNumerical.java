import java.sql.SQLException;

public abstract class RemoteNumerical extends Remote {
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

    protected boolean insert(String className, String hashCode, String value) {
        return this.rc.executeUpdate("INSERT INTO "+className+" (id, value) VALUES ("+hashCode + ", "+value+");");
    }

    protected boolean updateValue(String className, String value) {
        return this.rc.executeUpdate("UPDATE "+ className+" SET value = " + value + " WHERE id = " + idCode() + ";");
    }

    @Override
    public boolean delete() {
        return this.rc.executeUpdate("DELETE FROM "+getClassName()+" WHERE id = "+ idCode() + ";");
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
