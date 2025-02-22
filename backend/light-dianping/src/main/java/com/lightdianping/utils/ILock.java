package com.lightdianping.utils;

public interface ILock {
    boolean tryLock(long timeoutSec);

    void unlock();
}
