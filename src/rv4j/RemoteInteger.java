package rv4j;

import rv4j.*;

import java.sql.*;
import java.util.*;

public class RemoteInteger extends RemoteSingleValue implements Comparable<RemoteInteger> {
    /*
    this class contains the comments regarding the structure of all RemoteSingleValue descendants.
    essentially, we must specify className, mainType, and the type returned from getValue(), plus redefine all
    necessary methods that take in a specific type, such as Integer.
     */

    // declare static final class-wide variables
    private static final String className = "RemoteInteger";
    private static final String mainType = "integer";

    // override Remote.getClassName()
    @Override
    public String getClassName() {
        return className;
    }

    // override RemoteSingleValue.getMainType() -- assuming this remote object has one value, this is the value in the
    // SQL schema.
    @Override
    public String getMainType() {
        return mainType;
    }

    // constructor 1, public constructor. we use RemoteConnection and create a remote Integer value.
    public RemoteInteger (RemoteConnection rc, Integer value) {
        super(rc);  // super() to Remote
        initialize(rc);  // RemoteSingleValue.initialize(), which uses getMainType() and getClassName()
        setValue(value);  // set our new remote value to value (failsafe against duplicate HashCodes)

        // since we are constructing a new instance of a remote integer, insert the value
        insertSingleValue(className, Integer.toString(hashCode()), Integer.toString(value));
    }

    // constructor 2, package-private. This is so methods and factories from this package can create RemoteIntegers.
    // the dummy is simply to differentiate this from the public constructor. It is not present on all other classes.
    RemoteInteger(RemoteConnection rc, Integer forcedHash, boolean dummy) {
        // we simply create a RemoteInteger object with an underlying Remote with a forcedHash.
        super(rc, forcedHash);
    }

    // return the remote value as a local Integer
    public Integer getValue() {
        try {
            // query the database, using RemoteSingleValue helper methods
            ResultSet rs = selectById(className);
            // return the "value" as an integer
            return rs.getInt("value");
        } catch (SQLException e) {
            return null;
        }
    }

    // set the remote value to be the same value as a local Integer
    public boolean setValue(Integer value) {
        // use RemoteSingleValue helper method :)
        return updateSingleValue(className, value.toString());
    }

    // load all instances of RemoteInteger from database, in a set
    public static Set<RemoteInteger> loadAll(RemoteConnection rc) {
        Set<RemoteInteger> s = new HashSet<>();
        try {
            // initialize the db in case it isn't initialized, which will result in a NullPointerException if it isn't
            rc.initialize("RemoteInteger", new String[]{"value"}, new String[]{"integer"});

            // select any values if there are any
            ResultSet rs = select(rc, className);
            while (rs.next()) {
                // add each value to the set, using the package-private constructor
                s.add(new RemoteInteger(rc, rs.getInt("id"), true));
            }
            return s;
        } catch (SQLException e) {
            // thrown when SQL goes wrong
            return null;
        } catch (NullPointerException e) {
            // thrown when there's no items in the ResultSet
            return s; // = an empty set
        }
    }

    // delete all instances of RemoteInteger from the database
    public static void deleteAll(RemoteConnection rc) {
        // it basically just drops table :/
        deleteTable(rc, className);
    }

    // for the sake of comparison between RemoteIntegers
    @Override
    public int compareTo(RemoteInteger other) {
        return getValue().compareTo(other.getValue());
    }
}
