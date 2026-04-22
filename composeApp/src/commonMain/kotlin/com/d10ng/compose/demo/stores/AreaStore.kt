package com.d10ng.compose.demo.stores

import com.d10ng.compose.demo.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

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
    ) {
        /** 用英文名的首字母作为索引 */
        val indexLetter: String get() = en.firstOrNull()?.uppercase() ?: "#"
    }

    private val json = Json { ignoreUnknownKeys = true }

    /** 国家或地区列表 */
    var list: List<Info> = emptyList()
        private set

    /** 是否已加载 */
    var loaded: Boolean = false
        private set

    @OptIn(ExperimentalResourceApi::class)
    suspend fun load() {
        if (loaded) return
        val bytes = Res.readBytes("files/area_phone_code.json")
        val jsonStr = bytes.decodeToString()
        val ls: MutableList<Info> = json.decodeFromString(jsonStr)
        list = ls.sortedBy { it.en }
        loaded = true
    }
}
