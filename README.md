#JESync Java Driver
This is the Java driver for [JESync](https://github.com/julman99/JESync)


NOTE: This is still work in progress and might contain bugs

Usage:


```java
//Create the client
JESyncClient jesync = new JESyncClient("127.0.0.1:11400"); //more than 1 host can be specified, the driver will hash the key and select a host

//This call will block the execution for at the most 10 seconds (second parameter)
JESyncLock lock = jesync.lock("test", 1, 10, 20); 
if(lock.isGranted()){
    //Lock granted for 20 seconds in the server (third parameter on the lock function)
    
    //Critical code
    //...blah()
    //End of critical code

    lock.release();
}
 
``` 

# Multiple Servers
You can use multiple servers with a single JESyncClient object. When lock() is called, the key is hashed against the server list and the key is locked in that particular server only. It is a simple way to "horizontally scale" inspired in memcached mechanism.
