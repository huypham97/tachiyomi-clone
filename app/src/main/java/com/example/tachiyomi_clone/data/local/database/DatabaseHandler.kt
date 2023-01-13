package com.example.tachiyomi_clone.data.local.database

import com.example.tachiyomi_clone.Database
import com.squareup.sqldelight.Query

interface DatabaseHandler {

    suspend fun <T> await(inTransaction: Boolean = false, block: suspend Database.() -> T): T

    suspend fun <T : Any> awaitOne(
        inTransaction: Boolean = false,
        block: suspend Database.() -> Query<T>
    ): T

    suspend fun <T : Any> awaitOneOrNull(
        inTransaction: Boolean = false,
        block: suspend Database.() -> Query<T>,
    ): T?
}