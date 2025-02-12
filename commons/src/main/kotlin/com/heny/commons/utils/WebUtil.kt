package com.heny.commons.utils

import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

object WebUtil {
    @JvmStatic
    fun renderObject(response: HttpServletResponse, status: Int, body: Any) {
        try {
            response.status = status
            response.contentType = "application/json"
            response.characterEncoding = "utf-8"
            val bodyJson = JSON.toJSONString(body)
            response.writer.print(bodyJson)
        } catch (e: IOException) {
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.writer.print(e.message)
        }
    }
}