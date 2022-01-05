package com.zhy.lib_common.utils;

public class LockerUtils {
    public static void waitFor(Object obj) {
        synchronized (obj) {
            try {
                obj.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void waitFor(Object obj, long millis) {
        synchronized (obj) {
            try {
                obj.wait(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyAll(Object obj) {
        synchronized (obj) {
            try {
                obj.notifyAll();
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notify(Object obj) {
        synchronized (obj) {
            try {
                obj.notify();
            } catch (IllegalMonitorStateException e) {
                e.printStackTrace();
            }
        }
    }
}
