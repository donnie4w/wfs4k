package com.wfs4k.httpserver

import com.wfs4k.handler.GetHandler
import com.wfs4k.handler.MockRouteHandler
import com.wfs4k.handler.TfHandler
import com.wfs4k.handler.UploadHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpContentCompressor
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.codec.http.HttpServerCodec



/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class ServerInitializer: ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel?) {
        var pipeline =  ch?.pipeline()
        pipeline?.addLast("timeout", ReadTimeoutHandler(5000));
        pipeline?.addLast("codec", HttpServerCodec())
        pipeline?.addLast(HttpContentCompressor(9));
        pipeline?.addLast("aggegator", HttpObjectAggregator(1024 * 1024 * 1024));
        pipeline?.addLast("handler", ServerHandler())
        Controller.addHandler("/mock", MockRouteHandler());
        Controller.addHandler("/r",GetHandler());
        Controller.addHandler("/u",UploadHandler());
        Controller.addHandler("/thrift",TfHandler());
    }

}