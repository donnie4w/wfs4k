package com.wfs4k.httpserver

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import com.wfs4k.exception.WfsException
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpHeaderNames.CONNECTION
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/6/28
 */


class ServerHandler : ChannelInboundHandlerAdapter {
    constructor() {
    }


    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        try {
            var request: HttpRequest? = null
            var uri: String = ""!!
            if (msg is HttpRequest) {
                request = msg as HttpRequest
                uri = request!!.uri()
                println(uri)
            }
            if (msg is HttpContent) {
                val rh = Controller.Route(uri)
                var bs: ByteArray? = null
                if (rh != null) {
                    bs = rh.handler(request!!, msg)
                } else {
                    bs = "404".toByteArray(io.netty.util.CharsetUtil.UTF_8)
                }
                val response = DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bs))
                response.headers().set(CONTENT_TYPE, "text/plain")
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes())
                if (HttpUtil.isKeepAlive(request!!)) {
                    response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                }
                ctx?.write(response)
                ctx?.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw WfsException(e)
        }

    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush();
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
        ctx.close()
    }


}