import java.util.*;
import java.sql.*;

public class RemoteDriver {

    public static void main(String[] args) {
        RemoteConnection rc = new SQLiteConnection("test.db");

        Set<RemoteSizedInteger> riSet12 = RemoteSizedInteger.loadAll(rc, 12);
        System.out.println("existing rsi 12: "+riSet12);
        Set<RemoteSizedInteger> riSet64 = RemoteSizedInteger.loadAll(rc, 64);
        System.out.println("existing rsi 64: "+riSet64);

        System.out.println("deleting all!");
        RemoteSizedInteger.deleteAll(rc);

        RemoteSizedInteger rsi12_1 = new RemoteSizedInteger(rc, 1920381L, 12);
        RemoteSizedInteger rsi12_2 = new RemoteSizedInteger(rc, 1920388L, 12);

        RemoteSizedInteger rsi64_1 = new RemoteSizedInteger(rc, 1920381L, 64);
        RemoteSizedInteger rsi64_2 = new RemoteSizedInteger(rc, 1920388L, 64);

        Set<RemoteSizedInteger> riSet12new = RemoteSizedInteger.loadAll(rc, 12);
        System.out.println("new rsi 12: "+riSet12new);
        Set<RemoteSizedInteger> riSet64new = RemoteSizedInteger.loadAll(rc, 64);
        System.out.println("new rsi 64: "+riSet64new);

        rsi12_1.setValue(4099L);
        rsi12_2.setValue(800000000000L);

        rsi64_1.setValue(4099L);
        rsi64_2.delete();

        Set<RemoteSizedInteger> riSet12new2 = RemoteSizedInteger.loadAll(rc, 12);
        System.out.println("new new rsi 12: "+riSet12new2);
        Set<RemoteSizedInteger> riSet64new2 = RemoteSizedInteger.loadAll(rc, 64);
        System.out.println("new new rsi 64: "+riSet64new2);
    }
}
