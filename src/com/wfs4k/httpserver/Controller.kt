package com.wfs4k.httpserver

import  java.util.concurrent.ConcurrentSkipListMap

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class Controller {
    companion object {
        var ctl: ConcurrentSkipListMap<String, RouteHandler> = ConcurrentSkipListMap<String, RouteHandler>()
        fun addHandler(regex: String, handler: RouteHandler) {
            ctl.put(regex, handler)
        }

        fun Route(uri: String): RouteHandler? {
            var retHandler: RouteHandler? = null
            for (key in ctl.keys) {
                if (parse(key, uri)) {
                    retHandler = ctl[key]
                }
            }
            return retHandler
        }

        fun parse(regex: String, url: String): Boolean {
            var regex = regex
            var url = url
            if (regex.endsWith("/")) {
                regex = regex + ".*?"
            }
            if (url.contains("?")) {
                url = url.substring(0, url.indexOf("?"))
            }
            var regexs = regex.split("/")
            var uris = url.split("/")
            if (regexs.size > uris.size) {
                return false
            } else {
                for (i in regexs.indices) {
                    if (i < regexs.size - 1 && !uris[i].matches(regexs[i].toRegex()) && regexs[i].length > 0) {
                        return false
                    } else if (i == regexs.size - 1) {
                        val res = url.substring(url.indexOf("/", i - 1) + 1, url.length)
                        if (regexs[i].length > 0 && res != regexs[i]
                                && !res.startsWith(if (regexs[i].endsWith("/")) regexs[i] else regexs[i] + "/")) {
                            return false
                        }
                    }
                }
                return true
            }
        }
    }
}