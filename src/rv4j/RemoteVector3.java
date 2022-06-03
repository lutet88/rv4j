package rv4j;

import rv4j.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RemoteVector3 extends Remote {

    public RemoteDouble x;
    public RemoteDouble y;
    public RemoteDouble z;

    protected RemoteVector3(RemoteConnection rc, int forcedHashCode, RemoteDouble x, RemoteDouble y, RemoteDouble z) {
        super(rc, forcedHashCode);
        initialize(rc);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected RemoteVector3(RemoteConnection rc) {
        super(rc);
        initialize(rc);
    }

    public RemoteVector3(RemoteConnection rc, double x, double y, double z) {
        super(rc);
        initialize(rc);
        this.x = new RemoteDouble(rc, x);
        this.y = new RemoteDouble(rc, y);
        this.z = new RemoteDouble(rc, z);
        insert();
    }

    public RemoteVector3(RemoteConnection rc, RemoteDouble x, RemoteDouble y, RemoteDouble z) {
        super(rc);
        initialize(rc);
        this.x = x;
        this.y = y;
        this.z = z;
        insert();
    }

    @Override
    public boolean initialize(RemoteConnection rc) {
        try {
            this.rc = rc;
            this.rc.initialize(getClassName(), new String[]{"x", "y", "z"}, new String[]{"float(53)", "float(53)", "float(53)"});
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void insert() {
        this.rc.executeUpdate("INSERT INTO "+getClassName()+" VALUES ("+this.idCode()+", "+this.x.idCode()+", "+this.y.idCode()+", "+this.z.idCode()+");");
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

    @Override
    String getClassName() {
        return "RemoteVector3";
    }

    public static Set<RemoteVector3> loadAll(RemoteConnection rc) {
        Set<RemoteVector3> s = new HashSet<>();
        try {
            rc.initialize("RemoteVector3", new String[]{"x", "y", "z"}, new String[]{"float(53)", "float(53)", "float(53)"});
            ResultSet rs = select(rc, "RemoteVector3");
            while (rs.next()) {
                RemoteDouble x = new RemoteDouble(rc, rs.getInt("x"));
                RemoteDouble y = new RemoteDouble(rc, rs.getInt("y"));
                RemoteDouble z = new RemoteDouble(rc, rs.getInt("z"));
                s.add(new RemoteVector3(rc, rs.getInt("id"), x, y, z));
            }
            return s;
        } catch (SQLException e) {
            return null;
        } catch (NullPointerException e) {
            return s;
        }
    }

    public static void deleteAll(RemoteConnection rc) {
        deleteTable(rc, "RemoteVector3");
    }

    @Override
    public String toString() {
        return "<" + x.toString() + ", " + y.toString() + ", " + z.toString() + ">";
    }
}
