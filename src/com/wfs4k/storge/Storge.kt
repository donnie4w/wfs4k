package com.wfs4k.storge

import com.wfs4k.conf.Config
import com.wfs4k.db.DBFactory
import com.wfs4k.exception.WfsRunTimeException
import com.wfs4k.util.Md5
import com.wfs4k.util.StringUtil
import com.wfs4k.util.Utils
import java.security.NoSuchAlgorithmException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import kotlin.experimental.and


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */

class StorgeFactory {
    companion object {
        private val storge = StorgeImpl()
        fun getStorge(): Storge {
            return storge
        }
    }
}

interface Storge {
    @Throws(WfsRunTimeException::class)
    fun appendData(bs: ByteArray, name: String, fileType: String, shardname: String?): Boolean

    @Throws(WfsRunTimeException::class)
    fun getData(name: String): ByteArray?

    @Throws(WfsRunTimeException::class)
    fun delData(name: String): String?

    @Throws(WfsRunTimeException::class)
    fun exist(name: String): Boolean
}

class StorgeImpl : Storge {
    var db: Db
    var fm: FileManager

    constructor(){
        db = Db(DBFactory.getDB(Config.fileData + "/fsdb", true))
        fm = FileManager(Config.maxFileSize, 1 shl 20, db)
    }

    override fun appendData(bs: ByteArray, name: String, fileType: String, shardname: String?): Boolean {
        if (Config.readonly) {
            return false
        }
        if (name == null || "" == name.trim { it <= ' ' } || bs == null || bs.size == 0) {
            return false
        }
        try {
            var fingerprint = Utils.fingerprint(name.toByteArray())
            synchronized(fingerprint.intern()) {
                if (StringUtil.notEmpty(shardname)) {
                    db.dbPutObj(StringUtil.toBytes(fingerprint)!!, Segment(name, fileType, null!!, shardname!!))
                    return true
                }
                val md5key = Md5.getMd5(bs)
                val sbs = Segment(name, fileType, md5key!!, "")
                db.dbPutObj(StringUtil.toBytes(fingerprint)!!, sbs)
                fm.setNameCache(fingerprint, sbs)
                synchronized(StringUtil.toString(md5key)!!.intern()) {
                    var mb = fm.getMd5Bean(md5key)
                    if (mb == null) {
                        val f = fm.getFdata()
                        val offset = f.AppendData(bs)
                        val size = bs.size
                        mb = Md5Bean(offset, size, f.fileName, null, Config.compress)
                        f.writeIdxMd5(md5key)
                    }
                    mb.AddQuote()
                    db.dbPutObj(md5key, mb)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun _AppendData(bs: ByteArray, f: Fdata) {
        val offset = f.AppendData(bs)
        val size = bs.size
        val md5key = Md5.getMd5(bs)
        val mb = Md5Bean(offset, size, f.fileName, null!!, Config.compress)
        f.writeIdxMd5(md5key!!)
        mb.AddQuote()
        db.dbPutObj(md5key, mb)
    }

    override fun getData(name: String): ByteArray? {
        try {
            val fingerprint = Utils.fingerprint(StringUtil.toBytes(name)!!)
            val segment = fm.getSegment(fingerprint)
            if (segment != null) {
                val shardname = segment.getShardName()
                if (StringUtil.notEmpty(shardname)) {
                    return null
                }
                val md5key = segment.getMd5()
                val mb = db.dbGetMd5Bean(md5key!!)
                val filename = mb?.getFileName()
                val fdata = fm.getFdataByName(filename!!)
                return fdata.getData(mb)
            }
        } catch (e: Exception) {

        }

        return null
    }

    @Throws(WfsRunTimeException::class)
    override fun delData(name: String): String? {
        val fingerprint = Utils.fingerprint(StringUtil.toBytes(name)!!)
        synchronized(fingerprint.intern()) {
            val segment = fm.getSegment(fingerprint)
            if (segment==null){
                return null
            }
            fm.removeNameCache(fingerprint)
            db.dbDel(StringUtil.toBytes(fingerprint)!!)
            val shardname = segment?.getShardName()
            val md5key = segment?.getMd5()
            synchronized(StringUtil.toString(md5key!!)!!.intern()) {
                val mb = fm.getMd5Bean(md5key)
                if (mb != null) {
                    mb.SubQuote()
                    if (mb.getQuoteNum().get() <= 0) {
                        fm.delMd5Bean(md5key)
                        fm.saveDel(mb)
                    } else {
                        db.dbPutObj(md5key, mb)
                    }
                }
            }
            return shardname
        }
    }

    @Throws(WfsRunTimeException::class)
    override fun exist(name: String): Boolean {
        val fingerprint = Utils.fingerprint(StringUtil.toBytes(name)!!)
        return fm.hasName(fingerprint)
    }
}