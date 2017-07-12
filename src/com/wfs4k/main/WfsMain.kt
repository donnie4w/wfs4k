package com.wfs4k.main

import com.wfs4k.conf.Config
import com.wfs4k.httpserver.Server

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
fun main(args: Array<String>) {
    println("----------------------------");
    println("--------- wfs"+ Config.version+" ---------");
    println("----------------------------");
    Server().run()
}