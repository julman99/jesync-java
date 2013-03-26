package com.github.julman99.jesync.client;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author julio
 */
public class JESyncClientTest extends TestCase {
    
    public JESyncClientTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of lock method, of class JESyncClient.
     */
    public void testSingleLock() throws Exception {
        JESyncClient jesync = new JESyncClient("127.0.0.1:11400");
        JESyncLock lock = jesync.lock("test", 1, 1, 20);
        assertTrue(lock.isGranted());
        assertTrue(lock.release());
        assertFalse(lock.isGranted());
    }
    
    /**
     * Test of lock method, of class JESyncClient.
     */
    public void testMultiLock() throws Exception {
        JESyncClient jesync = new JESyncClient("127.0.0.1:11400");
        
        JESyncLock lock = jesync.lock("test", 1, 1, 20);
        assertTrue(lock.isGranted());
        
        int n = 5;
        for(int i=0;i<n;i++){
            JESyncLock lock2 = jesync.lock("test", 1, 0, 20);
            assertFalse(lock2.isGranted());
        }
        
        assertTrue(lock.release());
        assertFalse(lock.isGranted());
    }
}
