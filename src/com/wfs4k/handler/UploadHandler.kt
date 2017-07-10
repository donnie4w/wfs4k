package com.wfs4k.handler

import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.multipart.FileUpload
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType
import java.io.IOException
import io.netty.handler.codec.http.multipart.InterfaceHttpData
import com.wfs4k.exception.WfsRunTimeException
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder
import com.wfs4k.storge.StorgeFactory
import com.wfs4k.util.StringUtil
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class UploadHandler : AbsHandler() {
    var storge = StorgeFactory.getStorge()
    override fun handler(request: HttpRequest, content: HttpContent): ByteArray? {
        try {
            val uri = request.uri()
            var name: String? = null
            if (uri.length > 3)
                name = uri.substring(3)

            val decoder = HttpPostRequestDecoder(DefaultHttpDataFactory(1L), request)
            decoder.offer(content)
            readHttpDataChunkByChunk(decoder, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return StringUtil.toBytes("200");
    }

    private fun readHttpDataChunkByChunk(decoder: HttpPostRequestDecoder, filename: String?) {
        try {
            while (decoder.hasNext()) {
                val data = decoder.next()
                if (data != null) {
                    try {
                        writeHttpData(data, filename)
                    } finally {
                        data.release()
                        break
                    }
                }
            }
        } catch (e: Exception) {
            throw WfsRunTimeException(e)
        }

    }

    @Throws(IOException::class)
    private fun writeHttpData(data: InterfaceHttpData, filename: String?) {
        var filename = filename
        if (data.httpDataType == HttpDataType.FileUpload) {
            val fileUpload = data as FileUpload
            if (fileUpload.isCompleted) {
                if (filename == null)
                    filename = fileUpload.filename
                val bs = fileUpload.byteBuf.array()
                storge.appendData(bs, filename!!, "", null)
            }
        }
    }
}