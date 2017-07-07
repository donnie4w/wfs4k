package com.wfs4k.util

import com.oracle.util.Checksums.update
import java.io.*
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.CRC32
import kotlin.experimental.and


/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class  Utils{
    companion object {
        fun fingerprint(bs: ByteArray): String {
            val crc32 = CRC32()
            crc32.update(bs)
            return java.lang.Long.toHexString(crc32.value)
        }

        fun compresseEncode(bs: ByteArray): ByteArray {
            return bs
        }

        fun compresseDecode(bs: ByteArray): ByteArray {
            return bs
        }
    }
}

class StringUtil {
    companion object {
        fun notEmpty(s: String?): Boolean {
            return s != null && s.trim { it <= ' ' }.length > 0
        }

        fun toBytes(s: String): ByteArray? {
            try {
                return s.toByteArray(charset("utf-8"))
            } catch (e: UnsupportedEncodingException) {
            }
            return null
        }

        fun toString(bs: ByteArray): String? {
            try {
                return String(bs,charset("utf-8"))
            } catch (e: UnsupportedEncodingException) {
            }
            return null
        }
    }
}

class ByteUtil{
    companion object {
        fun int2bytes(row: Int): ByteArray {
            val bb = ByteBuffer.allocate(4)
            bb.putInt(row)
            return bb.array()
        }

        fun bytes2int(bs: ByteArray): Int {
            val bb = ByteBuffer.wrap(bs)
            return bb.getInt()
        }
    }
}

class SerializeUtil {
    companion object {
        @Throws(Exception::class)
        fun encode(obj: Any): ByteArray {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(obj)
            val bs = baos.toByteArray()
            baos.close()
            return bs
        }

        @Throws(Exception::class)
        fun <T> decode(bs: ByteArray?): T {
            val bais = ByteArrayInputStream(bs)
            val ois = ObjectInputStream(bais)
            val o = ois.readObject() as T
            bais.close()
            return o
        }
    }
}

class Md5{
    companion object {
        fun getMd5(bs: ByteArray): ByteArray? {
            try {
                val md = MessageDigest.getInstance("MD5")
                return md.digest(bs)
            } catch (e: NoSuchAlgorithmException) {
            }

            return null
        }

        fun getMd5(msg: String): String? {
            var ret: String? = null
            try {
                val result = msg.toByteArray(charset("utf-8"))
                val md = MessageDigest.getInstance("MD5")
                val digest = md.digest(result)
                ret = byte2hex(digest)
            } catch (e: Exception) {
            }
            return ret
        }

        fun byte2hex(b: ByteArray): String {
            val sb = StringBuilder()
            for (n in b.indices) {
                val s1 = Integer.toHexString((b[n] and 0x0f).toInt())
                val s2 = Integer.toHexString(b[n].toInt() shr 4 and 0x0f)
                sb.append(s2)
                sb.append(s1)
            }
            return sb.toString().toUpperCase()
        }
    }
}