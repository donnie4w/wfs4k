package com.wfs4k.conf

import com.sun.org.apache.xpath.internal.operations.Bool

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/6
 */

class Config {
    companion object {
        val version = "1.0.0"
        var port: Int = 3434
        var chiplimit: Int = 0
        var maxDataSize: Long = 1 shl 30
        var fileData: String = "data"
        var maxFileSize: Long = 1 shl 24
        var readonly: Boolean = false
        var keepalive: Int = 0
        var readPerSecond: Long = 60
        var serverReadTimeout: Int = 5
        var bind: String = "0.0.0.0"
        var compress: Boolean = false

        private val _p = "-p"
        private val _MD = "-MD"
        private val _max = "-max"
        private var _fileData = "-fileData"
        private var _readonly = "-readonly"
        private var _keepalive = "-keepalive"
        private var _rps = "-rps"
        private var _srt = "-srt"
        private var _bind = "-bind"
        private var _compress = "-compress"


        fun parse(args: Array<String>): Boolean {
            try {
                if (args != null && args.size > 0) {
                    println(args.size)
                    for (i in 0..args.size) {
                        when {
                            args[i] == _p -> port = args[i + 1].toInt()
                            args[i] == _MD -> maxDataSize = args[i + 1].toLong()
                            args[i] == _max -> maxFileSize = args[i + 1].toLong()
                            args[i] == _fileData -> fileData = args[i + 1]
                            args[i] == _readonly -> readonly = true
                            args[i] == _bind -> bind = args[i]
                        }
                    }
                }
            } catch (e: Exception) {
                return false
            }
            return true
        }
    }

}