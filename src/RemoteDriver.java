import java.util.*;
import java.sql.*;

public class RemoteDriver {

    public static void main(String[] args) {
        RemoteConnection rc = new SQLiteConnection("test.db");

        Set<RemoteFloat> riSet = RemoteFloat.loadAll(rc);
        System.out.println("existing RemoteFloat: "+riSet);

        RemoteFloat ri = new RemoteFloat(rc, 14.4f);
        RemoteFloat riStored = new RemoteFloat(rc, (float) (Math.random() * 200));

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.setValue(-15.0f);

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.setValue(200.4f);

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        ri.delete();

        System.out.println("value of ri: " + ri.getValue());
        System.out.println("value of ri.idCode(): "+ri.idCode());

        System.out.println("value of stored ri: "+riStored.getValue());
    }]
}
