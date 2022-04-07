package com.zxd.etuhttp.wrapper.utils;

import java.util.concurrent.TimeUnit;

/**
 * author: zxd
 * created on: 2021/11/3 14:06
 * description:
 */
public class LogTime {

    private long startNs;

    public LogTime() {
        this.startNs = System.nanoTime();
    }

    public long tookMs() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
    }
}
