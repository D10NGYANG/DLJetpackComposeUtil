package com.d10ng.compose.demo.stores

import com.d10ng.app.base.ActivityManager
import com.d10ng.app.resource.getAssetsJSONArray
import com.d10ng.common.transform.toPinYin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * 国家或地区数据仓库
 * @Author d10ng
 * @Date 2023/9/18 10:57
 */
object AreaStore {

    @Serializable
    data class Info(
        @SerialName("code")
        var code: Int = 0,
        @SerialName("en")
        var en: String = "",
        @SerialName("locale")
        var locale: String = "",
        @SerialName("tw")
        var tw: String = "",
        @SerialName("zh")
        var zh: String = "",

        // 拼音
        var py: String = ""
    )

    /** 自定义规则的JSON工具 */
    val json by lazy {
        Json {
            // 忽略JSON字符串里有但data class中没有的key
            ignoreUnknownKeys = true
            // 如果接收到的JSON字符串的value为null，但是data class中的对应属性不能为null，那就使用属性的默认值
            coerceInputValues = true
            // 如果创建data class实例时有些属性没有赋值，那就使用默认值进行转换成JSON字符串
            encodeDefaults = true
        }
    }

    /** 国家或地区列表 */
    var list: List<Info>

    init {
        val jsonObj = ActivityManager.top().value!!.getAssetsJSONArray("area_phone_code.json").toString()
        val ls: MutableList<Info> = json.decodeFromString(jsonObj)
        ls.forEach { item -> item.py = item.zh.toPinYin() }
        list = ls.sortedBy { it.py }
    }
}