package com.imhanjie.v2ex.api

import android.annotation.SuppressLint
import com.imhanjie.support.e
import com.imhanjie.v2ex.parser.Parser
import com.imhanjie.v2ex.parser.impl.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ParserInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var url = request.url().toString()
        val originMethod = request.method()
        var response = chain.proceed(request)
        e("intercept: $url")

        var isFailRequest = false

        // 单独处理重定向逻辑
        if (response.code() == 302) {
            url = ApiServer.BASE_URL + response.header("location")
            if (isNeedRedirect(url)) {
                response.close()
                val redirectRequest = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                isFailRequest = true
                response = chain.proceed(redirectRequest)
            }
        }

        val targetParser = getParser(originMethod, url)
        if (targetParser == null) {
            // 未发现 html 解析器，说明不需要解析，直接返回 response
            return response
        } else {
            // 需要进行 html 解析
            val html = response.body()!!.string()
            val obj: Any
            return try {
                obj = targetParser.parser(html)
                if (isFailRequest) {
                    response.recreateFailJsonResponse(obj.toString())
                } else {
                    response.recreateSuccessJsonResponse(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                response.recreateFailJsonResponse("数据解析错误")
            }
        }
    }

    /**
     * 根据 url 获取 html 解析器
     *
     * @param url 请求的 url
     * @return 若返回 null 说明不需要解析，反之需要解析
     */
    @SuppressLint("DefaultLocale")
    private fun getParser(method: String, url: String): Parser? {
        return with(url) {
            if (startsWith("${ApiServer.BASE_URL}/recent?p=")
                || startsWith("${ApiServer.BASE_URL}/?tab=")
            ) {
                LatestTopicsParser()
            } else if (startsWith("${ApiServer.BASE_URL}/t/")) {
                TopicParser()
            } else if (startsWith("${ApiServer.BASE_URL}/go/")) {
                NodeTopicsParser()
            } else if (equals("${ApiServer.BASE_URL}/signin") && method.toUpperCase() == "GET") {
                SignInParser()
            } else if (equals("${ApiServer.BASE_URL}/signin/cooldown")) {
                CoolDownParser()
            } else if (equals("${ApiServer.BASE_URL}/settings")) {
                SettingsParser()
            } else {
                null
            }
        }
    }

    private fun isNeedRedirect(url: String): Boolean {
        return with(url) {
            equals("${ApiServer.BASE_URL}/signin/cooldown")
        }
    }

}