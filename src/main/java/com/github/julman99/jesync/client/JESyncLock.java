package com.github.julman99.jesync.client;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author julio
 */
public final class JESyncLock {

    private final String key;
    private final PooledStringSocket socket;
    private int concurrent;
    private int maxConcurrent;
    private int waiting;
    private boolean granted;
    private Date expiresAt;
    private JESyncClient client;

    public String getKey() {
        return key;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public int getMaxConcurrent() {
        return maxConcurrent;
    }

    public int getWaiting() {
        return waiting;
    }

    public boolean isGranted() {
        return granted;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public JESyncClient getClient() {
        return client;
    }

    void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    void setMaxConcurrent(int maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    void setGranted(boolean granted) {
        this.granted = granted;
    }

    void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    void setClient(JESyncClient client) {
        this.client = client;
    }
    
    
    JESyncLock(String key, PooledStringSocket socket) {
        this.key = key;
        this.socket = socket;
    }
    
    public boolean release() throws IOException{
        if(!this.isGranted()){
            throw new RuntimeException("not locked");
        }
        
        String command = String.format("release %s", this.key);
        
        this.socket.writeLine(command);
        String response = this.socket.readLine();
        
        String[] splitted = response.split(" ");
        boolean released = splitted[0].toLowerCase().equals("released");
        this.granted = !released;
        this.concurrent = Integer.parseInt(splitted[1]);
        this.waiting = Integer.parseInt(splitted[2]);
        
        this.socket.freeSocket();
        
        return released;
    }
    
    public JESyncLock renew() throws IOException{
        this.setGranted(false);
        return this.client.lock(key, maxConcurrent, 0, 0);
    }

    @Override
    protected void finalize() throws Throwable {
        if(this.isGranted()){
            release();
        }
        super.finalize();
    }
    
    
    
}
