import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/* OK This doesn't actually work because I can't figure out how to instantiate specific classes based on
   what I store in database. I think it's possible but it takes too much java.reflect >:(((
 */

public class RemoteArray<T extends Remote> extends Remote implements Iterable<T>{
    private final int size;
    private T dummyItem;

    public RemoteArray(RemoteConnection rc, int size) {
        super(rc);
        this.size = size;
        initialize(rc);
    }

    public RemoteArray(RemoteConnection rc, T[] arr) {
        super(rc);
        this.size = arr.length;
        initialize(rc);
    }

    private RemoteArray(RemoteConnection rc, int size, int forcedHash) {
        super(rc);
        this.forcedHashCode = forcedHash;
        this.size = size;
        this.rc = rc;
    }

    @Override
    public boolean initialize(RemoteConnection rc) {
        this.rc = rc;

        String[] values = new String[size];
        String[] types = new String[size];
        for (int i = 0; i < size; i++) {
             values[i] = "value"+i;
             types[i] = "integer";
        }

        try {
            this.rc.initialize(getClassName(), values, types);
            this.rc.initialize("RemoteArrayIndex", new String[]{"size"}, new String[]{"integer"});
        } catch (SQLException e) {
            throw new RuntimeException("RemoteArray not initialized: "+e);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(getClassName()).append(" VALUES (").append(idCode()).append(", ");
        String prefix = "";
        for (int i = 0; i < size; i++) {
            sb.append(prefix);
            prefix = ", ";
            sb.append(0);
        }
        sb.append(");");

        return this.rc.executeUpdate(sb.toString()) &&
               this.rc.executeUpdate("INSERT INTO RemoteArrayIndex VALUES ("+idCode()+", "+size()+");");
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
        return "RemoteArray"+this.size;
    }

    public int size() {
        return this.size;
    }

    public boolean set(int index, T value) {
        int code = value.idCode();

        return rc.executeUpdate("UPDATE "+getClassName()+" SET value"+index+" = "+code+ " where id = "+idCode());
    }

    public T get(int index) {
        try {
            ResultSet rs = rc.executeQuery("SELECT value" + index + " FROM " + getClassName() + " where id = " + idCode() + ";");
            int forcedCode = rs.getInt("value" + index);

            T instance = (T) dummyItem.getClass().newInstance();

            instance.setHash(forcedCode);
            instance.setRc(rc);

            return instance;

        } catch (SQLException e) {
            return null;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        ResultSet rs = rc.executeQuery("SELECT * FROM RemoteArray"+size()+");");
        return new RemoteIterator<>(rc, rs, size, "value");
    }

    public static Set<RemoteArray> loadAll(RemoteConnection rc) {
        Set<Integer> sizes = new HashSet<>();

        try {
            rc.initialize("RemoteArrayIndex", new String[]{"size"}, new String[]{"integer"});
            ResultSet rs1 = rc.executeQuery("SELECT * FROM RemoteArrayIndex;");
            while (rs1.next()) {
                sizes.add(rs1.getInt("size"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Set<RemoteArray> s = new HashSet<>();

        for (int size : sizes) {
            try {
                String[] values = new String[size];
                String[] types = new String[size];
                for (int i = 0; i < size; i++) {
                    values[i] = "value"+i;
                    types[i] = "integer";
                }

                rc.initialize("RemoteArray"+size, values, types);
                ResultSet rs = select(rc, "RemoteArray"+size);
                while (rs.next()) {
                    s.add(new RemoteArray(rc, size, rs.getInt("id")));
                }
            } catch (SQLException | NullPointerException ignored) {
            }
        }

        return s;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw new RuntimeException("RemoteArray.forEach(Consumer<? super T> action) is not implemented");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String prefix = "";
        for (int i = 0; i < size; i++) {
            sb.append(prefix);
            prefix = ", ";
            sb.append(get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
