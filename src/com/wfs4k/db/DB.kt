package com.wfs4k.db

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/6
 */
interface DB {
    fun exist(key: ByteArray): Boolean
    fun put(key: ByteArray, value: ByteArray)
    fun get(key: ByteArray): ByteArray?
    fun del(key: ByteArray)
}

class DBImpl : DB {
    var leveldb: LevelDB? = null

    constructor(path: String) {
        leveldb = LevelDB(path)
    }

    override fun exist(key: ByteArray): Boolean {
        return leveldb?.get(key) != null
    }

    override fun put(key: ByteArray, value: ByteArray) {
        leveldb?.put(key, value)
    }

    override fun get(key: ByteArray): ByteArray? {
        return leveldb?.get(key)
    }

    override fun del(key: ByteArray) {
        leveldb?.delete(key)
    }
}

public class DBFactory() {
    companion object {
        fun getDB(path: String, backup: Boolean): DB {
            return DBImpl(path)
        }
    }
}