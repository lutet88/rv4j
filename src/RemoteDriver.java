import java.util.*;
import java.sql.*;

public class RemoteDriver {

    public static void main(String[] args) {
        RemoteConnection rc = new SQLiteConnection("test.db");

        Set<RemoteInteger> riSet = RemoteInteger.loadAll(rc);
        System.out.println("existing RemoteIntegers: "+riSet);

        RemoteInteger ri = new RemoteInteger(rc, 14);
        RemoteInteger riStored = new RemoteInteger(rc, (int) (Math.random() * 200));

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.setValue(-15);

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.setValue(200);

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.delete();

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());
    }
}
