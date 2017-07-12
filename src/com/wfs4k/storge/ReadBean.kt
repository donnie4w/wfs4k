package com.wfs4k.storge

import java.util.concurrent.atomic.AtomicLong



/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class  ReadBean{
    var rps = AtomicLong(1)// read per second
    var lastReadTime = AtomicLong(0)

    fun add() {
        if (System.nanoTime() - this.lastReadTime.get() < 60) {
            rps.incrementAndGet()
        } else {
            rps.set(1)
        }
        lastReadTime.set(System.nanoTime())
    }
}