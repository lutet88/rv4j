import java.util.*;

public class RemoteTestingDriver {

    public static void main(String[] args) {
        testRArray();
    }

    public static void testRArray() {
        RemoteConnection rc = new SQLiteConnection("test.db");
        Set<RemoteArray> remoteArrays = RemoteArray.loadAll(rc);
        System.out.println("Remote Arrays: " +remoteArrays);

        RemoteArray<RemoteInteger> arr = new RemoteArray<>(rc, 4);
        arr.set(0, new RemoteInteger(rc, 4));
        arr.set(1, new RemoteInteger(rc, 400));
        arr.set(2, new RemoteInteger(rc, 40000));
        arr.set(3, new RemoteInteger(rc, -400000));

        System.out.println(arr);

        rc.close();
    }

    public static void testRString() {
        RemoteConnection rc = new SQLiteConnection("test.db");
        Set<RemoteString> rStrSet = RemoteString.loadAll(rc);
        System.out.println("existing strings: "+rStrSet);

        RemoteString.deleteAll(rc);

        RemoteString rs1 = new RemoteString(rc, "hello");
        RemoteString rs2 = new RemoteString(rc, "");
        RemoteString rs3 = new RemoteString(rc, "98jas9d211io3 12oi3u 1298 891x");
        RemoteString rs4 = new RemoteString(rc, "爱的覅骄傲圣诞节覅伺机待发");

        rStrSet = RemoteString.loadAll(rc);
        System.out.println("new strings: "+rStrSet);

        rs2.setValue("Robert'); DROP TABLE Students;--");
        rs3.setValue(String.valueOf((char) 1) + ((char) 1) + ((char) 1));
        rs4.delete();

        rStrSet = RemoteString.loadAll(rc);
        System.out.println("new strings: "+rStrSet);

        System.out.println(rs3.compareTo(rs1));

        rc.close();
    }

    public static void testRChar() {
        RemoteConnection rc = new SQLiteConnection("test.db");
        Set<RemoteCharacter> rCharSet = RemoteCharacter.loadAll(rc);
        System.out.println("existing chars: "+rCharSet);

        RemoteCharacter.deleteAll(rc);

        RemoteCharacter rc1 = new RemoteCharacter(rc, 'A');
        RemoteCharacter rc2 = new RemoteCharacter(rc, (char) 1);
        RemoteCharacter rc3 = new RemoteCharacter(rc, '为');
        RemoteCharacter rc4 = new RemoteCharacter(rc, '3');

        rCharSet = RemoteCharacter.loadAll(rc);
        System.out.println("new chars: "+rCharSet);

        rc2.setValue((char) 144);
        rc3.setValue('C');
        rc4.delete();

        rCharSet = RemoteCharacter.loadAll(rc);
        System.out.println("new chars: "+rCharSet);

        System.out.println(rc3.compareTo(rc1));

        rc.close();
    }

    public static void testRSI() {
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

        rc.close();
    }
}
