package com.d10ng.compose.demo.stores

import com.d10ng.app.resource.getAssetsJSONArray
import com.d10ng.common.transform.json
import com.d10ng.common.transform.toPinYin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    /** 国家或地区列表 */
    var list: List<Info>

    init {
        val jsonObj = getAssetsJSONArray("area_phone_code.json").toString()
        val ls: MutableList<Info> = json.decodeFromString(jsonObj)
        ls.forEach { item -> item.py = item.zh.toPinYin() }
        list = ls.sortedBy { it.py }
    }
}