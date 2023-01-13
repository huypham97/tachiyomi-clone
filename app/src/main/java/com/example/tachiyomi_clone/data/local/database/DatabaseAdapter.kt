package com.example.tachiyomi_clone.data.local.database

import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy
import com.squareup.sqldelight.ColumnAdapter

private const val listOfStringsSeparator = ", "
val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> = if (databaseValue.isEmpty()) {
        listOf()
    } else {
        databaseValue.split(listOfStringsSeparator)
    }

    override fun encode(value: List<String>): String =
        value.joinToString(separator = listOfStringsSeparator)

}

val updateStrategyAdapter = object : ColumnAdapter<UpdateStrategy, Long> {
    private val enumValues by lazy { UpdateStrategy.values() }

    override fun decode(databaseValue: Long): UpdateStrategy =
        enumValues.getOrElse(databaseValue.toInt()) { UpdateStrategy.ALWAYS_UPDATE }

    override fun encode(value: UpdateStrategy): Long = value.ordinal.toLong()
}