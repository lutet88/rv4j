public class RemoteFactory {
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
