package com.heny.commons.domain.enums


/**
 * @author :Yozuru
 */
@Deprecated(
    message = "糟糕的设计，使用Spring提供的HttpStatus代替",
    replaceWith = ReplaceWith("org.springframework.http.HttpStatus")
)
enum class HttpCode(val code: Int, val message: String) {
    SUCCESS(200, "操作成功。"),
    BAD_REQUEST(400, "请求参数有误。"),
    UNAUTHORIZED(401, "请求未授权，请重新登录。"),
    FORBIDDEN(403, "请求被拒绝。"),
    SERVER_ERROR(500, "服务器出错");

    companion object {
        val codeToMessageMap = HttpCode.entries.associate { it.code to it.message }
    }
}
