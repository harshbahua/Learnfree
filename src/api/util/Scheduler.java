/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.util;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Monil Gudhka
 */
public class Scheduler {
    private final TaskQueue queue = new TaskQueue();
    private final SchedulerThread thread = new SchedulerThread(queue);

    
    private final Object threadReaper = new Object() {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            synchronized(queue) {
                thread.newTasksMayBeScheduled = false;
                queue.notify(); // In case queue is empty.
            }
        }
    };

    private final static AtomicInteger nextSerialNumber = new AtomicInteger(0);
    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    
    public Scheduler() {
        this("Scheduler-" + serialNumber());
    }
    public Scheduler(boolean isDaemon) {
        this("Scheduler-" + serialNumber(), isDaemon);
    }
    public Scheduler(String name) {
        this(name, false);
    }
    public Scheduler(String name, boolean isDaemon) {
        thread.setName(name);
        thread.setDaemon(isDaemon);
        thread.start();
    }

    

    public void setSelfDestruction(boolean enable){
        thread.self_destruct = enable;
    }
    public boolean isSelfDestructEnabled(){
        return thread.self_destruct;
    }
    public void schedule(Task task, long delay) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        sched(task, System.currentTimeMillis()+delay, 0);
    }
    public void schedule(Task task, Date time) {
        sched(task, time.getTime(), 0);
    }
    public void schedule(Task task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis()+delay, -period);
    }
    public void schedule(Task task, Date firstTime, long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), -period);
    }
    public void scheduleAtFixedRate(Task task, long delay, long period) {
        if (delay < 0)
            throw new IllegalArgumentException("Negative delay.");
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis()+delay, period);
    }
    public void scheduleAtFixedRate(Task task, Date firstTime,
                                    long period) {
        if (period <= 0)
            throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), period);
    }

    
    private void sched(Task task, long time, long period) {
        if (time < 0)
            throw new IllegalArgumentException("Illegal execution time.");

        // Constrain value of period sufficiently to prevent numeric
        // overflow while still being effectively infinitely large.
        if (Math.abs(period) > (Long.MAX_VALUE >> 1))
            period >>= 1;

        synchronized(queue) {
            if (!thread.newTasksMayBeScheduled)
                throw new IllegalStateException("Scheduler already cancelled.");

            synchronized(task.lock) {
                if (task.state != Task.VIRGIN){
                    if(task.state == Task.SCHEDULED){
                        queue.quickRemove(task);
                    }else{
                        throw new IllegalStateException(
                        "Task already cancelled");
                    }
                }
                task.nextExecutionTime = time;
                task.period = period;
                task.state = Task.SCHEDULED;
            }

            queue.add(task);
            if (queue.getMin() == task)
                queue.notify();
        }
    }

    public void cancel() {
        synchronized(queue) {
            thread.newTasksMayBeScheduled = false;
            queue.clear();
            queue.notify();  // In case queue was already empty.
        }
    }
    public int purge() {
         int result = 0;

         synchronized(queue) {
             for (int i = queue.size(); i > 0; i--) {
                 if (queue.get(i).state == Task.CANCELLED) {
                     queue.quickRemove(i);
                     result++;
                 }
             }

             if (result != 0)
                 queue.heapify();
         }

         return result;
     }
}


class SchedulerThread extends Thread {
    boolean newTasksMayBeScheduled = true;
    boolean self_destruct = false;
    private TaskQueue queue;

    SchedulerThread(TaskQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } finally {
            // Someone killed this Thread, behave as if Scheduler cancelled
            synchronized(queue) {
                newTasksMayBeScheduled = false;
                queue.clear();  // Eliminate obsolete references
            }
        }
    }

    private void mainLoop() {
        while (true) {
            try {
                Task task;
                boolean taskFired;
                synchronized(queue) {
                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled && !self_destruct)
                        queue.wait();
                    if (queue.isEmpty())
                        break; // Queue is empty and will forever remain; die

                    // Queue nonempty; look at first evt and do the right thing
                    long currentTime, executionTime;
                    task = queue.getMin();
                    synchronized(task.lock) {
                        if (task.state == Task.CANCELLED) {
                            queue.removeMin();
                            continue;  // No action required, poll queue again
                        }
                        currentTime = System.currentTimeMillis();
                        executionTime = task.nextExecutionTime;
                        if (taskFired = (executionTime<=currentTime)) {
                            if (task.period == 0) { // Non-repeating, remove
                                queue.removeMin();
                                task.state = Task.EXECUTED;
                            } else { // Repeating task, reschedule
                                queue.rescheduleMin(
                                  task.period<0 ? currentTime   - task.period
                                                : executionTime + task.period);
                            }
                        }
                    }
                    if (!taskFired) // Task hasn't yet fired; wait
                        queue.wait(executionTime - currentTime);
                }
                if (taskFired)  // Task fired; run it, holding no locks
                    task.run();
            } catch(InterruptedException e) {
            }
        }
    }
}


class TaskQueue {
    private Task[] queue = new Task[128];
    private int size = 0;

    int size() {
        return size;
    }

    void add(Task task) {
        // Grow backing store if necessary
        if (size + 1 == queue.length)
            queue = Arrays.copyOf(queue, 2*queue.length);

        queue[++size] = task;
        fixUp(size);
    }
    int get(Task task){
        for(int i=0; i<queue.length; i++){
            if(queue[i] == task)
                return i;
        }
        return -1;
    }

    Task getMin() {
        return queue[1];
    }

    Task get(int i) {
        return queue[i];
    }
    void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;  // Drop extra reference to prevent memory leak
        fixDown(1);
    }
    void quickRemove(int i) {
        assert i <= size;

        queue[i] = queue[size];
        queue[size--] = null;  // Drop extra ref to prevent memory leak
    }
    void quickRemove(Task task) {
        int i = get(task);
        if(i >= 0)
            quickRemove(i);
    }
    void rescheduleMin(long newTime) {
        queue[1].nextExecutionTime = newTime;
        fixDown(1);
    }
    boolean isEmpty() {
        return size==0;
    }
    void clear() {
        // Null out task references to prevent memory leak
        for (int i=1; i<=size; i++) {
            queue[i] = null;
        }
        size = 0;
    }
    private void fixUp(int k) {
        while (k > 1) {
            int j = k >> 1;
            if (queue[j].nextExecutionTime <= queue[k].nextExecutionTime)
                break;
            Task tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }
    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size && j > 0) {
            if (j < size &&
                queue[j].nextExecutionTime > queue[j+1].nextExecutionTime)
                j++; // j indexes smallest kid
            if (queue[k].nextExecutionTime <= queue[j].nextExecutionTime)
                break;
            Task tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }
    void heapify() {
        for (int i = size/2; i >= 1; i--) {
            fixDown(i);
        }
    }
}