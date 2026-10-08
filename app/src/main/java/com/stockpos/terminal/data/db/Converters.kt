package com.stockpos.terminal.data.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: String): List<String> = value.split("|||")

    @TypeConverter
    fun toStringList(list: List<String>): String = list.joinToString("|||")
}
