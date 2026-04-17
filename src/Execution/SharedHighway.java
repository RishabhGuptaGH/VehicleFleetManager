package Execution;

import java.util.concurrent.locks.ReentrantLock;

public class SharedHighway {
    public static int totalHighwayDistance = 0;
    private static final ReentrantLock lock = new ReentrantLock();
    private static boolean synchronizationEnabled = false;

    public static void setSynchronizationEnabled(boolean enabled) {
        synchronizationEnabled = enabled;
    }

    public static boolean isSynchronizationEnabled() {
        return synchronizationEnabled;
    }

    public static void updateDistance(int amount) {
        if (synchronizationEnabled) {
            lock.lock();
            try {
                totalHighwayDistance += amount;
            } finally {
                lock.unlock();
            }
        } else {
            int temp = totalHighwayDistance;
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            totalHighwayDistance = temp + amount;
        }
    }

    public static void reset() {
        if (synchronizationEnabled) {
            lock.lock();
            try {
                totalHighwayDistance = 0;
            } finally {
                lock.unlock();
            }
        } else {
            totalHighwayDistance = 0;
        }
    }
}
