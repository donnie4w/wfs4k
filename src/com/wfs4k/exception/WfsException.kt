package com.wfs4k.exception

import java.lang.Exception

/**
 * @Author donnie {donnie4w@gmail.com}
 * @Date 2017/7/7
 */
class WfsException : Exception {
    constructor(cause: Throwable?) : super(cause) {
    }

    constructor(message: String?) : super(message) {
    }
}

class WfsRunTimeException : RuntimeException {
    constructor(cause: Throwable?) : super(cause) {
    }

    constructor(message: String?) : super(message) {
    }
}