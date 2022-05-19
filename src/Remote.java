import java.util.Set;

public abstract class Remote {
    Integer forcedHashCode;
    RemoteConnection rc;

    public abstract boolean initialize(RemoteConnection rc);
    public abstract boolean delete();
    public abstract int idCode();

    public static Set<? extends Remote> loadAll(RemoteConnection rc) {
        return null;
    }
}
