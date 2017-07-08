package com.wfs4k.db

import org.iq80.leveldb.*

import com.sun.corba.se.impl.encoding.CodeSetConversion.impl
import org.iq80.leveldb.impl.Iq80DBFactory.*
import java.io.*

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class LevelDB {
    var db: org.iq80.leveldb.DB

    constructor(dbfile: String) {
        var options = Options()
        options.createIfMissing(true)
        db = factory.open(File(dbfile), options)
    }

    fun put(key: ByteArray, value: ByteArray) {
        db.put(key, value)
    }
    fun get(key: ByteArray): ByteArray? {
        return db.get(key)
    }

    fun delete(key:ByteArray) {
         db.delete(key)
    }
    fun close(key:ByteArray){
        db.close()
    }
}