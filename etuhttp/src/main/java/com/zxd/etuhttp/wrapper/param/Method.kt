package com.zxd.etuhttp.wrapper.param

/**
 * author: zxd
 * created on: 2021/2/1 15:53
 * description:
 */
enum class Method {
    GET,HEAD,PUT,POST,PATCH,DELETE;

    open fun isGet(): Boolean{
        return name == "GET"
    }

    open fun isPost(): Boolean {
        return name == "POST"
    }

    open fun isHead(): Boolean {
        return name == "HEAD"
    }

    open fun isPut(): Boolean {
        return name == "PUT"
    }

    open fun isPatch(): Boolean {
        return name == "PATCH"
    }

    open fun isDelete(): Boolean {
        return name == "DELETE"
    }
}