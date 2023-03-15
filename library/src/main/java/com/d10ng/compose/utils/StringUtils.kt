package com.d10ng.compose.utils

/**
 * 字符串填充到指定长度
 * @receiver [String]
 * @param length [Int] 需要填充到的长度
 * @param filler [Char] 填充物
 * @param isInStart [Boolean] 是否从开头处填充，
 *                            true：开头；
 *                            false：结尾
 * @param isForced [Boolean] 是否强制性要只保留指定长度字符串，
 *                           true：如果字符串本身已经比输入长度[length]长，也要只截取其中的部分；
 *                           false：如果字符串本身已经比输入长度[length]长，那也不管；
 * @return String
 */
internal fun String.up2Length(
    length: Int,
    filler: Char = '0',
    isInStart: Boolean = true,
    isForced: Boolean = true
): String {
    val result = StringBuilder()
    if (!isInStart) result.append(this)
    if (this.length < length) {
        for (i in 0 until length - this.length) {
            result.append(filler)
        }
    }
    if (isInStart) result.append(this)
    return if (isForced) {
        if (isInStart) result.toString().substring(result.length - length)
        else result.toString().substring(0, length)
    } else result.toString()
}