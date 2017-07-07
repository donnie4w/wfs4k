package com.wfs4k.httpserver

import com.wfs4k.conf.Config
import com.wfs4k.log.Logger
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/6/28
 */

class Server {
    var logger: Logger = Logger.getLogger()
    fun run() {
        var bossGroup = NioEventLoopGroup()
        var workerGroup = NioEventLoopGroup()
        try {
            var b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
            b.channel(NioServerSocketChannel::class.java)
            val port = Config.port
            logger.log("wfs listen:", port)
            b.childHandler(ServerInitializer())
            b.bind(port).sync().channel().closeFuture().sync();
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

fun main(args: Array<String>) {
    Server().run()
}