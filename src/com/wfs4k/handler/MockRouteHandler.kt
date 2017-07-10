package com.wfs4k.handler

import com.wfs4k.util.StringUtil
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpRequest
import java.io.UnsupportedEncodingException
import io.netty.handler.codec.http.QueryStringDecoder


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class MockRouteHandler : AbsHandler() {
    override fun handler(request: HttpRequest, content: HttpContent): ByteArray? {
        try {
            println("mock uri:" + request.uri())
            println("mock method:" + request.method())
            val reqDecoder = QueryStringDecoder(request.uri())
            var data = reqDecoder.parameters().get("data")!!.get(0)
            var s = ""
            s = StringUtil.toString(getBody(content))!!;
            println("data:" + data)
            return ("ret," + s).toByteArray()
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}