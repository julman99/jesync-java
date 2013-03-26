package com.github.julman99.jesync.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple socket factory.
 * Right now it acts as a factory of sockets. In the future this should be 
 * a socket pool that reuses non closed sockets. 
 * @author julio
 */
public class SocketPool {
    
    private String host;
    private int port;

    public SocketPool(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    public Socket pullSocket() throws UnknownHostException, IOException{
        return new Socket(host, port);
    }
    
    public void pushSocket(Socket socket){
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
