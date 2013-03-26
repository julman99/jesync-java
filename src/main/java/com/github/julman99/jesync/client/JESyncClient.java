package com.github.julman99.jesync.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author julio
 */
public class JESyncClient {
    
    private static final int DEFAULT_PORT = 11400;
    
    private List<SocketPool> sockets = new ArrayList<SocketPool>();

    List<SocketPool> getSockets() {
        return sockets;
    }
    
    public JESyncClient(String host, int port){
        this(host + ":" + port);
    }
    
    public JESyncClient(String... hosts){
        for(String host: hosts){
            String splitted[] = host.split(":");
            String hostName = splitted[0];
            int port = DEFAULT_PORT;
            if(splitted.length > 1){
                port = Integer.parseInt(splitted[1]);
            }
            
            SocketPool pool = new SocketPool(hostName, port);
            sockets.add(pool);
        }
    }
    
    private PooledStringSocket getSocket(String key) throws IOException {
        int count = this.sockets.size();
        int index = 0;
        if(count > 0){
            if(count > 1){
                //More than 1 server, hash the key to select the appropiate one
                index = Math.abs(key.hashCode() % this.sockets.size());
            }
            
            return new PooledStringSocket(this.sockets.get(index));
        } else {
            return null;
        }
    }
    
    public JESyncLock lock(String key, int maxConcurrent, int maxGrantWait, int grantExpires) throws IOException{
        PooledStringSocket s = getSocket(key);
        if(s != null){
            String command = String.format("lock %s %d %d %d", key, maxConcurrent, maxGrantWait, grantExpires);
            
            s.writeLine(command);
            
            String response = s.readLine();
            
            String[] splitted = response.split(" ");
            JESyncLock lock = new JESyncLock(key, s);
            lock.setClient(this);
            lock.setGranted(splitted[0].equals("GRANTED"));
            lock.setConcurrent(Integer.parseInt(splitted[1]));
            lock.setWaiting(Integer.parseInt(splitted[2]));
            if(lock.isGranted()){
                lock.setExpiresAt(new Date(System.currentTimeMillis() + Integer.parseInt(splitted[4]) * 1000));
            }
            
            return lock;
        }
        return null;
    }
    
    
}
