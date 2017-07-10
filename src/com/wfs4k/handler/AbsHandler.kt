package com.wfs4k.handler

import com.wfs4k.httpserver.RouteHandler
import com.wfs4k.storge.Storge
import com.wfs4k.storge.StorgeFactory
import com.wfs4k.util.StringUtil
import io.netty.handler.codec.http.HttpContent
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpRequest


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
abstract class AbsHandler : RouteHandler {
    fun getBody(content: HttpContent): ByteArray {
        val buf = content.content()
        val body = ByteArray(buf.capacity())
        buf.readBytes(body)
        buf.release()
        return body
    }
}

class GetHandler : AbsHandler() {
    var storge: Storge = StorgeFactory.getStorge()
    override fun handler(request: HttpRequest, content: HttpContent): ByteArray? {
        val uri = request.uri()
        var bs: ByteArray? = null
        if (uri.length > 3) {
            val name = uri.substring(3)
            bs = storge.getData(name)
        }
        if (bs != null) {
            return bs
        } else {
            return StringUtil.toBytes("404")
        }
    }
}