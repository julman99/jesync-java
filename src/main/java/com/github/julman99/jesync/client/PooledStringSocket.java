package com.github.julman99.jesync.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author julio
 */
public class PooledStringSocket {
    
    private final SocketPool pool;
    private final Socket socket;
    private final OutputStreamWriter out;
    private final BufferedReader in;
    private boolean closed = false;

    public PooledStringSocket(SocketPool pool) throws UnknownHostException, IOException {
        this.pool = pool;
        this.socket = pool.pullSocket();
        this.out = new OutputStreamWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }
    
    public synchronized void freeSocket(){
        pool.pushSocket(this.socket);
        closed = true;
    }
    
    public void writeLine(String line) throws IOException{
        if(!closed){
            this.out.write(line);
            this.out.write("\n");
            this.out.flush();
        } else {
            throw new IOException("The socket is closed");
        }
    }
    
    public String readLine() throws IOException{
        if(!closed){
            return this.in.readLine();
        } else {
            throw new IOException("The socket is closed");
        }
    }
    
}
