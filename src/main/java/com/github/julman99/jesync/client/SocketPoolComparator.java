package com.github.julman99.jesync.client;

import java.util.Comparator;

/**
 *
 * @author julio
 */
public class SocketPoolComparator implements Comparator<SocketPool>{

    private String getFullHost(SocketPool pool){
        return pool.getHost() + ":" + pool.getPort();
    }
    
    public int compare(SocketPool t, SocketPool t1) {
        return getFullHost(t).compareToIgnoreCase(getFullHost(t1));
    }
    
}
