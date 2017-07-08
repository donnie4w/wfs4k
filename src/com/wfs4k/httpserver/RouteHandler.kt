package com.wfs4k.httpserver

import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpRequest

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
interface RouteHandler {
    fun handler(request: HttpRequest, content: HttpContent): ByteArray?
}
