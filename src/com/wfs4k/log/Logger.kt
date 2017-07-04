package com.wfs4k.log

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/6/28
 */

class Logger {
    var log: java.util.logging.Logger? = null

    constructor(cn: String) {
        log = java.util.logging.Logger.getLogger(cn);
    }

    companion object Factory {
        fun getLogger(): Logger {
            return Logger(Thread.currentThread().getStackTrace()[2].getClassName());
        }
    }

    fun objs2str(args: Array<out Any>): String {
        var sb: StringBuilder = StringBuilder()
        for (s in args) {
            sb.append(s.toString()).append(" ")
        }
        return sb.toString()
    }

    fun log(vararg args: Any) = log?.info(objs2str(args))
}

fun main(args: Array<String>) {
    var logger: Logger = Logger.getLogger()
    logger.log("test1", "test2")
    logger.log("test1", "test2")
    logger.log("test1", "test2")
}