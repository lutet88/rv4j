import javax.crypto.NullCipher;
import java.util.*;

public class RemoteTestingDriver {

    public static void main(String[] args) {
        testRVec3();
        testRChar();
        testRSI();
        testRString();

        System.out.println("tests done!");
    }

    public static void testRVec3() {
        RemoteConnection rc = new SQLiteConnection("test.db");

        RemoteDouble.deleteAll(rc);
        System.out.println("deleted doubles");
        RemoteVector3.deleteAll(rc);
        System.out.println("deleted vec3s");

        RemoteDouble rd = new RemoteDouble(rc, 0.99);
        RemoteVector3 rvec1 = new RemoteVector3(rc, 0.5, rd.getValue(), 0.7);
        RemoteVector3 rvec2 = new RemoteVector3(rc, 0.9, 0.4, 0.2);
        RemoteVector3 rvec3 = new RemoteVector3(rc, rvec1.y, rvec1.x, rvec2.z);
        RemoteVector3 rvec4 = new RemoteVector3(rc, new RemoteDouble(rc, 1928393.298), rvec3.z, rvec3.x);

        Set<RemoteVector3> rVecSet = RemoteVector3.loadAll(rc);
        System.out.println("new vecs: "+rVecSet);

        Set<RemoteDouble> rDoubleSet = RemoteDouble.loadAll(rc);
        System.out.println("new doubles: "+rDoubleSet);

        rd.delete();
        rvec1.x.setValue(0.0);
        rvec2.y.setValue(rvec3.y.getValue());
        rvec2.z.delete();
        rvec4.delete();

        try {
            rVecSet = RemoteVector3.loadAll(rc);
            System.out.println("new vecs: " + rVecSet);

            rDoubleSet = RemoteDouble.loadAll(rc);
            System.out.println("new doubles: " + rDoubleSet);
        } catch (NullPointerException e) {
            System.out.println("nullpointer detected. this is working properly");
        }

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
