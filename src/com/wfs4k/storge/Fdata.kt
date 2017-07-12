package com.wfs4k.storge

import com.wfs4k.exception.WfsRunTimeException
import com.google.common.util.concurrent.Striped.lock
import com.wfs4k.conf.Config
import com.wfs4k.util.Utils
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.ReadWriteLock


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class Fdata {
    var fileName: String
    var curPoint: Long = 0
    var f: JFile
    var idxf: JFile
    var rb: ReadBean
    var rwl: ReadWriteLock = ReentrantReadWriteLock()

    constructor(fileName: String, curPoint: Long, f: JFile, idxf: JFile, rb: ReadBean) {
        this.fileName = fileName
        this.curPoint = curPoint
        this.f = f
        this.idxf = idxf
        this.rb = rb
    }

    fun getAndSetCurPoint(size: Long): Long {
        rwl.writeLock().lock()
        try {
            val offset = this.curPoint
            this.curPoint = offset + size
            return offset
        } finally {
            rwl.writeLock().unlock()
        }
    }

    fun fileSize(): Long {
        return this.curPoint
    }

    fun closeFile() {
        try {
            this.f.close()
        } catch (e: Exception) {
        }

    }

    fun AppendData(bs: ByteArray): Long {
        var bs: ByteArray = bs
        try {
            if (Config.compress) {
                bs = Utils.compresseEncode(bs)
            }
            val size = bs.size
            val offset = getAndSetCurPoint(size.toLong())
            this.f.writeAt(offset, bs)
            return offset
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        }

    }

    fun writeIdxMd5(md5key: ByteArray) {
        try {
            this.idxf.append(md5key)
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        }

    }

    @Synchronized fun closeAndDelete() {
        this.f.close()
        this.idxf.close()
        this.f.remove()
        this.idxf.remove()
    }

    fun getData(md5Bean: Md5Bean): ByteArray? {
        var bs = this.f.read(md5Bean.getOffset(), md5Bean.getSize())
        if (md5Bean.isCompress()) {
            bs = Utils.compresseDecode(bs!!)
        }
        this.rb.add()
        return bs
    }

    fun compact(chip: Int): Boolean {
        return false
    }

    fun strongCompact(chip: Int): Boolean {
        return false
    }
}