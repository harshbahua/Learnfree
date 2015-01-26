/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.util;

/**
 *
 * @author Monil Gudhka
 */
public abstract class Task implements Runnable {
    static final int VIRGIN = 0;
    static final int SCHEDULED   = 1;
    static final int EXECUTED    = 2;
    static final int CANCELLED   = 3;
    
    final Object lock = new Object();
    int state = VIRGIN;
    long nextExecutionTime;
    long period = 0;
    
    protected Task() {
    }

    @Override
    public abstract void run();

    public boolean cancel() {
        synchronized(lock) {
            boolean result = (state == SCHEDULED);
            state = CANCELLED;
            return result;
        }
    }

    public long scheduledExecutionTime() {
        synchronized(lock) {
            return (period < 0 ? nextExecutionTime + period
                               : nextExecutionTime - period);
        }
    }
}