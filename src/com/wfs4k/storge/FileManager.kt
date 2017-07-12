package com.wfs4k.storge

import com.wfs4k.conf.Config
import com.wfs4k.lru.LRUCache
import com.wfs4k.util.ByteUtil
import com.wfs4k.util.StringUtil
import java.util.concurrent.ConcurrentHashMap


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class  FileManager{
    companion object {
        val _del_ = "_del_"
        val _current_file_ = "_current_file_"
        val _file_sequence_ = "_file_sequence_"
        val _dat_ = "_dat_"
        val _slave_ = "_slave_"
    }


    private var fileMaxSize: Long = 0
    private var currFileName: String? = null
    private var nameCache: LRUCache<Any, Any>
    private val fileMap = ConcurrentHashMap<String, Fdata>()
    private val md5map = ConcurrentHashMap<ByteArray, Md5Bean>()
    var db: Db

    constructor(fileMaxSize: Long, cacheSize: Int, db: Db) {
        this.fileMaxSize = fileMaxSize
        nameCache = LRUCache<Any, Any>(cacheSize)
        this.db = db
    }

    fun getSegment(fingerprint: String): Segment? {
        var _segment = this.nameCache?.get(fingerprint)
        var segment: Segment? = if(_segment!=null) _segment as Segment else null
        if (segment != null) {
            return segment
        }
        segment = db.dbGetSegment(StringUtil.toBytes(fingerprint)!!)
        if (segment != null) {
            this.nameCache?.put(fingerprint, segment)
        }
        return segment
    }

    fun hasName(name: String): Boolean {
        try {
            var b = this.nameCache.containsKey(name)
            if (!b) {
                b = db.dbExsit(StringUtil.toBytes(name)!!)
                if (b) {
                    val segment = db.dbGetSegment(StringUtil.toBytes(name)!!)
                    this.nameCache.put(name, segment!!)
                }
            }
            return b
        } catch (e: Exception) {
        }
        return false
    }

    fun setNameCache(fingerprint: String, segment: Segment) {
        this.nameCache.put(fingerprint, segment)
    }

    fun removeNameCache(fingerprint: String) {
        this.nameCache.remove(fingerprint)
    }

    @Synchronized fun getFdata(): Fdata {
        val f = this.fileMap.get(_current_file_)
        if (f != null) {
            if (f!!.fileSize() < this.fileMaxSize) {
                return f
            }
        } else {
            val bs = db.dbGut(StringUtil.toBytes(_current_file_)!!)
            if (bs != null) {
                val filename = StringUtil.toString(bs)
                val fdata = _openFdataFile(filename!!)
                if (fdata.fileSize() < this.fileMaxSize) {
                    this.fileMap.put(filename, fdata)
                    this.fileMap.put(_current_file_, fdata)
                    db.dbPut(StringUtil.toBytes(_current_file_)!!, StringUtil.toBytes(filename)!!)
                    this.currFileName = filename
                }
                return fdata
            }
        }
        return _newFdata(true)
    }

    fun _openFdataFile(filename: String): Fdata {
        val idxfilename = filename.replace(".dat", ".idx")
        val currFile = JFile(filename)
        val currIdxFile = JFile(idxfilename)
        return Fdata(filename, currFile.fileSize(), currFile, currIdxFile, ReadBean())
    }

    fun _newFdata(isCurrent: Boolean): Fdata {
        val sub = System.nanoTime()
        db.dbPut(StringUtil.toBytes(_dat_ + sub)!!, byteArrayOf(0))
        val filename = Config.fileData + "/" + sub + ".dat"
        val fdata = _openFdataFile(filename)
        this.fileMap.put(fdata.fileName!!, fdata)
        if (isCurrent) {
            this.fileMap.put(_current_file_, fdata)
            db.dbPut(StringUtil.toBytes(_current_file_)!!, StringUtil.toBytes(fdata.fileName)!!)
            this.currFileName = filename
        }
        return fdata
    }

    fun getMd5Bean(md5key: ByteArray): Md5Bean? {
        var mb = this.md5map.get(md5key)
        if (mb != null) {
            return mb
        }
        mb = db.dbGetMd5Bean(md5key)
        if (mb != null) {
            this.md5map.put(md5key, mb)
        }
        return mb
    }

    @Synchronized fun delMd5Bean(md5key: ByteArray) {
        this.md5map.remove(md5key)
        db.dbDel(md5key)
    }

    @Synchronized fun getFdataByName(filename: String): Fdata {
        var fdata = this.fileMap.get(filename)
        if (fdata == null) {
            fdata = _openFdataFile(filename)
            this.fileMap.put(filename, fdata)
        }
        return fdata
    }

    fun saveDel(mb: Md5Bean) {
        val filename = mb.getFileName()
        synchronized(filename!!.intern()) {
            val size = mb.getSize()
            val fileKey = _del_ + filename!!
            val bs = db.dbGut(StringUtil.toBytes(fileKey)!!)
            if (bs != null) {
                val i = ByteUtil.bytes2int(bs) + size
                db.dbPut(StringUtil.toBytes(fileKey)!!, ByteUtil.int2bytes(i))
            } else {
                db.dbPut(StringUtil.toBytes(fileKey)!!, ByteUtil.int2bytes(size))
            }
        }
    }

    fun getCurrFileName(): String {
        return currFileName!!
    }
}