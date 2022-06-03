package rv4j;

import rv4j.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class RemoteIterator<T extends Remote> implements Iterator<T> {
    private ResultSet rs;
    private RemoteConnection rc;
    private int size;
    private int index;
    private String key;

    private T dummy;

    RemoteIterator(RemoteConnection rc, ResultSet rs, int size, String key) {
        this.key = key;
        this.size = size;
        this.rs = rs;
    }

    @Override
    public boolean hasNext() {
        return !(index >= size);
    }

    @Override
    public T next() {
        if (!hasNext()) return null;
        index ++;

        int idCode;

        try {
            idCode = rs.getInt(key+index);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            T instance = (T) dummy.getClass().newInstance();
            instance.setRc(rc);
            instance.setHash(idCode);

            return instance;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void remove() {
        throw new RuntimeException("RemoteArray does not support remove()");
    }
}
