package com.example.game_zone.ui.utils.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GustosConverter {
    private val gson = Gson()

    fun listToJson(gustos: List<String>): String {
        return gson.toJson(gustos)
    }

    fun jsonToList(json: String): List<String> {
        if (json.isEmpty()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}