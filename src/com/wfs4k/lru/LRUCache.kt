package com.wfs4k.lru

import com.wfs4k.log.Logger

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/6/28
 */
open class LRUCache<K, V> : LinkedHashMap<K, V> {
    var cacheSize: Int = 0

    constructor(initialCapacity: Int) : super(16, 0.75f, true) {
        this.cacheSize = initialCapacity;
    }

    override fun removeEldestEntry(eldest: Map.Entry<K, V>): Boolean {
        return size > cacheSize;
    }
}

fun main(arg :Array<String>){
    var lc:LRUCache<String,String> = LRUCache<String,String>(5)
    lc.put("wu","dong")
    lc.put("wu1","dong1")
    lc.put("wu2","dong2")
    lc.put("wu3","dong3")
    for (i in 1..10){
        lc.put("wu"+i,"dong"+i)
    }
    println(lc)
    var logger: Logger = Logger.getLogger()
    logger.log("test1", "test2")
}