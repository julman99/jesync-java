#JESync Java Driver
This is the Java driver for [JESync](https://github.com/julman99/JESync)


NOTE: This is still work in progress and might contain bugs

Usage:


```java
JESyncClient jesync = new JESyncClient("127.0.0.1:11400"); //more than 1 host can be specified, the driver will hash the key and select a host
JESyncLock lock = jesync.lock("test", 1, 1, 20); //key, concurrent locks, timeout for lock, grant time (see JESync docs)
if(lock.isGranted()){
    //Critical code here
    lock.release();
}
 
``` 