## rv4j
### remote variables 4 java
have you ever wanted to store all your Java variables somewhere else? like on the cloud? rv4j is the
solution for you! with this pointless library, you can create remote versions of any Java object, stored on
an SQLite or MySQL/MariaDB database! 

### tutorial
```java
public static void main(String[] args) {
    RemoteConnection rc = new SQLiteConnection("test.db");
    
    Set<RemoteInteger> returnedSet = RemoteInteger.loadAll(rc);
    System.out.println("existing RemoteIntegers: "+returnedSet);
    
    RemoteInteger ri = new RemoteInteger(rc, 14);
    
    System.out.println("value of ri: " + ri);
}
```

### api reference
* Remote
  * RemoteSingleValue
    * RemoteCharacter
    * RemoteDouble
    * RemoteFloat
    * RemoteInteger
    * RemoteSizedInteger
    * RemoteString
  * RemoteVector3
* RemoteConnection
  * SQLiteConnection
  * (future) MySQLConnection

Just read the code it's well-commented and not too long

### making your own remote class

1. extend abstract class `Remote`
2. implement methods

refer to `RemoteInteger.java` for a commented example.

### why should you use this
**legit reasons**
* permanently store variables between runs of a java program
* redundant backup of runtime variables on a database 
  * (maybe you're running a program for years and bit-flips can actually cause problems?)
* have multiple devices/applications access variables at once
  
  
**less legit reasons**
* connect to a remote database then claim your CS project uses "cloud technology"
* launch a bunch of threads then have them access a `RemoteQueue` (crappy concurrency)
* dramatically slow down your java project, so you can charge your customers to optimize it
* run java programs on computers with no available memory but lots of free space

### why did I make this

I can't think of anything else for this assignment (SQL final project in AT Data Structures)
