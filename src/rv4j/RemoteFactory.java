package rv4j;

import rv4j.*;

public class RemoteFactory {
    /*
    This is a helper class that allows Remote classes that point to other Remote classes to create each other.
    This is unused, as of now. I couldn't get RemoteArray to work with all types, although implementing it without
    generics would be simple.
     */

    public static Remote createRemote(RemoteConnection rc, String type, int id) {
        if (type.equals("RemoteInteger")) {
            return new RemoteInteger(rc, id, false);
        } else if (type.equals("RemoteCharacter")) {
            return new RemoteCharacter(rc, id);
        } else if (type.equals("RemoteFloat")) {
            return new RemoteFloat(rc, id);
        } else if (type.equals("RemoteDouble")) {
            return new RemoteDouble(rc, id);
        } else if (type.contains("RemoteSizedInteger")) {
            int size = Integer.parseInt(type.split("SizedInteger")[1]);
            return new RemoteSizedInteger(rc, id, size);
        } else if (type.equals("RemoteString")) {
            return new RemoteString(rc, id);
        } else {
            throw new RuntimeException("not valid remote type!");
        }
    }
}
