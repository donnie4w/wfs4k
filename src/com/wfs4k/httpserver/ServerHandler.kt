package com.wfs4k.httpserver

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/6/28
 */


class ServerHandler : ChannelInboundHandlerAdapter {
    constructor() {
    }


    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {

    }

    override fun channelReadComplete(ctx:ChannelHandlerContext){
        ctx.flush();
    }
    override fun exceptionCaught(ctx:ChannelHandlerContext,cause:Throwable?){
        ctx.close()
    }



}