package com.wfs4k.storge

import com.wfs4k.exception.WfsRunTimeException
import java.nio.channels.FileLock
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.io.File
import java.nio.channels.FileChannel

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class JFile {
    var file: File
    var filename: String = ""
    var out: RandomAccessFile
    var fc: FileChannel

    constructor(filename: String) {
        try {
            file = File(filename)
            out = RandomAccessFile(file, "rw")
            fc = out.channel
        } catch (e: FileNotFoundException) {
            throw WfsRunTimeException(e)
        }
    }

    @Throws(Exception::class)
    fun writeAt(offset: Long, bs: ByteArray) {
        val fl = fc.lock(offset, bs.size.toLong(), false)
        try {
            fc.force(true)
            out.seek(offset)
            out.write(bs)
        } finally {
            fl.release()
        }
    }

    @Throws(Exception::class)
    fun append(bs: ByteArray) {
        fc.force(true)
        file.appendBytes(bs)
    }

    fun fileSize(): Long {
        try {
            return out.length()
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        }
    }

    fun close() {
        try {
            fc.close()
        } catch (e: Exception) {
        }
        try {
            out.close()
        } catch (e: Exception) {
        }
    }

    fun read(offset: Long, length: Int): ByteArray {
        var fl: FileLock? = null
        try {
            fl = fc.lock(offset, length.toLong(), true)
            val bs = ByteArray(length)
            out.seek(offset)
            out.read(bs)
            return bs
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        } finally {
            if (fl != null)
                try {
                    fl.release()
                } catch (e: Exception) {
                    throw WfsRunTimeException(e)
                }
        }
    }

    fun remove() {
        this.file.delete()
    }
}