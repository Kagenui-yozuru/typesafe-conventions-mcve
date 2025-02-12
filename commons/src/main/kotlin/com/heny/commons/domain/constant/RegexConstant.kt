package com.heny.commons.domain.constant

/**
 * @author :Yozuru
 * @since :2024/7/24 下午5:51
 */

const val USERNAME_REGEX: String = "^[a-zA-Z]\\w{4,12}$"
const val USERNAME_REGEX_DESC: String = "用户名必须以字母开头，长度为5-13位"
const val PASSWORD_REGEX: String = "^[a-zA-Z0-9]{6,16}$"
const val PASSWORD_REGEX_DESC: String = "密码必须为6-16位数字或字母"
const val NAME_REGEX: String = "^[\u4e00-\u9fa5]{2,8}$"
const val PHONE_REGEX: String = "^1[3-9]\\d{9}$"
const val CAR_NUMBER_REGEX: String =
    "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]$"

