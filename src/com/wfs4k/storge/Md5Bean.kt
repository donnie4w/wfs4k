package com.wfs4k.storge

import java.util.concurrent.atomic.AtomicInteger


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class Md5Bean : java.io.Serializable, Cloneable {
    companion object {
        val serialVersionUID = 1L
    }
    private var offset: Long = 0
    private var size: Int = 0
    private var fileName: String? = null
    private var quoteNum = AtomicInteger(0)
    private var sequence: ByteArray? = null
    private var compress: Boolean = false

    constructor(offset: Long, size: Int, filename: String, sequence: ByteArray?, compress: Boolean) {
        this.offset = offset
        this.size = size
        this.fileName = filename
        this.sequence = sequence
        this.compress = compress
    }

    fun getOffset(): Long {
        return offset
    }

    fun setOffset(offset: Long) {
        this.offset = offset
    }

    fun getSize(): Int {
        return size
    }

    fun setSize(size: Int) {
        this.size = size
    }

    fun getFileName(): String? {
        return fileName
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    fun getQuoteNum(): AtomicInteger {
        return quoteNum
    }

    fun setQuoteNum(quoteNum: AtomicInteger) {
        this.quoteNum = quoteNum
    }

    fun getSequence(): ByteArray? {
        return sequence
    }

    fun setSequence(sequence: ByteArray) {
        this.sequence = sequence
    }

    fun isCompress(): Boolean {
        return compress
    }

    fun setCompress(compress: Boolean) {
        this.compress = compress
    }

    fun AddQuote() {
        this.quoteNum.incrementAndGet()
    }

    fun SubQuote() {
        this.quoteNum.addAndGet(-1)
    }
}