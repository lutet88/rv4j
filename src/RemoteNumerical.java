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

    public boolean setValue(Object value) {
        return this.rc.executeUpdate("UPDATE "+getClassName()+" SET value = " + value.toString() + " WHERE id = " + idCode() + ";");
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
