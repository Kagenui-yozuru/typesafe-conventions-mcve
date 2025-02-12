package com.heny.commons.domain.constant

import java.time.Duration

/**
 * @author :Yozuru
 * @since :2024/7/24 上午9:28
 */
object CacheConstant {
    const val LOGIN_USER_CACHE_NAME: String = "login_user:"

    @JvmField
    val LOGIN_USER_CACHE_EXPIRE: Duration = Duration.ofDays(1)

    @JvmField
    val LOGIN_USER_CACHE_REFRESH: Duration = Duration.ofMinutes(1410)
}
