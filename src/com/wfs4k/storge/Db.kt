package com.wfs4k.storge

import com.wfs4k.db.DB
import com.wfs4k.exception.WfsRunTimeException
import com.wfs4k.exception.WfsException
import com.wfs4k.util.SerializeUtil


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class Db {
    var db: DB

    constructor(db: DB) {
        this.db = db
    }

    fun dbPutObj(key: ByteArray, obj: Any) {
        try {
            db.put(key, SerializeUtil.encode(obj))
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        }

    }

    fun dbGetSegment(key: ByteArray): Segment? {
        try {
            var segment: Segment = SerializeUtil.decode(db.get(key))
            return segment
        } catch (e: Exception) {
        }
        return null
    }

    fun dbGetMd5Bean(key: ByteArray): Md5Bean? {
        try {
            var mb: Md5Bean = SerializeUtil.decode(db.get(key))
            return mb
        } catch (e: Exception) {
            return null
        }
    }

    fun dbDel(key: ByteArray) {
        try {
            db.del(key)
        } catch (e: WfsException) {
            throw WfsRunTimeException(e)
        }
    }

    fun dbPut(key: ByteArray, value: ByteArray) {
        try {
            db?.put(key, value)
        } catch (e: WfsException) {
            throw WfsRunTimeException(e)
        }
    }

    fun dbGut(key: ByteArray): ByteArray? {
        try {
            return db.get(key)
        } catch (e: WfsException) {
            throw WfsRunTimeException(e)
        }
    }

    fun dbExsit(key: ByteArray): Boolean {
        try {
            return db.exist(key)
        } catch (e: WfsException) {
            throw WfsRunTimeException(e)
        }
    }
}