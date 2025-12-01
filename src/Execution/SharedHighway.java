package Execution;

import java.util.concurrent.locks.ReentrantLock;

public class SharedHighway {
    public static int totalHighwayDistance = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void updateDistance(int amount) {
//        lock.lock();
        try {
            int temp = totalHighwayDistance;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            totalHighwayDistance = temp + amount;
        } finally {
//            lock.unlock();
        }
    }

    public static void reset() {
        totalHighwayDistance = 0;
    }
}