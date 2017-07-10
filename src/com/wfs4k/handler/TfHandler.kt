package com.wfs4k.handler

import com.wfs.protocol.IWfs
import com.wfs.protocol.WfsAck
import com.wfs.protocol.WfsCmd
import com.wfs.protocol.WfsFile
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpRequest
import org.apache.thrift.transport.TIOStreamTransport
import java.io.ByteArrayInputStream
import org.apache.thrift.protocol.TCompactProtocol
import org.apache.thrift.protocol.TProtocolFactory
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.InputStream
import org.apache.thrift.TException
import com.wfs4k.storge.StorgeFactory

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/8
 */
class TfHandler : AbsHandler() {
    companion object {
        protected var inProtocolFactoryTCompact: TProtocolFactory = TCompactProtocol.Factory()
        protected var outProtocolFactoryTCompact: TProtocolFactory = TCompactProtocol.Factory()
    }

    override fun handler(request: HttpRequest, content: HttpContent): ByteArray? {
        var out: ByteArrayOutputStream? = null
        var `in`: InputStream? = null
        try {
            val processor = IWfs.Processor(WfsImpl())
            `in` = ByteArrayInputStream(getBody(content))
            out = ByteArrayOutputStream()
            val transport = TIOStreamTransport(`in`, out)
            val inProtocol = inProtocolFactoryTCompact.getProtocol(transport)
            val outProtocol = outProtocolFactoryTCompact.getProtocol(transport)
            processor.process(inProtocol, outProtocol)
            transport.flush()
            return out!!.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeStream(`in`, out)
        }
        return null
    }

    fun closeStream(vararg ios: Closeable?) {
        if (ios != null && ios.size > 0) {
            for (io in ios) {
                try {
                    io?.close()
                } catch (e: Exception) {
                }
            }
        }
    }
}

class WfsImpl : IWfs.Iface {
    var storge = StorgeFactory.getStorge()

    fun newInstance(): WfsImpl {
        return WfsImpl()
    }

    @Throws(TException::class)
    override fun wfsPost(wf: WfsFile): WfsAck {
        val bs = wf.getFileBody()
        val name = wf.getName()
        val fileType = wf.getFileType()
        storge.appendData(bs, name, fileType, null)
        val wa = WfsAck()
        return wa
    }

    @Throws(TException::class)
    override fun wfsRead(name: String): WfsFile {
        val wf = WfsFile()
        wf.setFileBody(storge.getData(name))
        return wf
    }

    @Throws(TException::class)
    override fun wfsDel(name: String): WfsAck {
        storge.delData(name)
        val wa = WfsAck()
        return wa
    }


    override fun wfsCmd(p0: WfsCmd?): WfsAck {
        return WfsAck()
    }
}