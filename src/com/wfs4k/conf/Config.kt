package com.wfs4k.conf

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/6
 */

class Config {
    companion object {
        val version  = "1.0.0"
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

        fun parse(){

        }
    }

}