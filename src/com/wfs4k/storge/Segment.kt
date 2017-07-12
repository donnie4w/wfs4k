package com.wfs4k.storge

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class Segment : java.io.Serializable,Cloneable {
    companion object {
        val serialVersionUID = 1L
    }

    private var id: Long = 0
    private var name: String? = null
    private var fileType: String? = null
    private var md5: ByteArray? = null
    private var shardName: String? = null

    constructor(name: String, fileType: String, md5: ByteArray, shardname: String){
        this.name = name
        this.fileType = fileType
        this.md5 = md5
        this.shardName = shardname
    }

    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getName(): String? {
        return this.name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getFileType(): String? {
        return fileType
    }

    fun setFileType(fileType: String) {
        this.fileType = fileType
    }

    fun getMd5(): ByteArray? {
        return md5
    }

    fun setMd5(md5: ByteArray) {
        this.md5 = md5
    }

    fun getShardName(): String? {
        return shardName
    }

    fun setShardName(shardName: String) {
        this.shardName = shardName
    }
}