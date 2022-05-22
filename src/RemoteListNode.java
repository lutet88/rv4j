import java.sql.SQLException;

public class RemoteListNode<T> extends Remote {

    protected RemoteListNode(RemoteConnection rc, int forcedHashCode) {
        super(rc, forcedHashCode);
    }

    protected RemoteListNode(RemoteConnection rc) {
        super(rc);
    }

    @Override
    public boolean initialize(RemoteConnection rc) {
        try {
            this.rc = rc;
            this.rc.initialize(getClassName(), new String[]{"value"}, new String[]{});
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public int idCode() {
        return 0;
    }

    @Override
    String getClassName() {
        return "RemoteListNode";
    }
}
