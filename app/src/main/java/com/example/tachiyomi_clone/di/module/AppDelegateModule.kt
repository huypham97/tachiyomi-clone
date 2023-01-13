package com.example.tachiyomi_clone.di.module

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.example.tachiyomi_clone.BuildConfig
import com.example.tachiyomi_clone.Database
import com.example.tachiyomi_clone.data.local.database.AndroidDatabaseHandler
import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.listOfStringsAdapter
import com.example.tachiyomi_clone.data.local.database.updateStrategyAdapter
import com.example.tachiyomi_clone.utils.Constant
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import data.Mangas
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton

@Module
class AppDelegateModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(Constant.SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): Database = Database(
        driver = driver,
        mangasAdapter = Mangas.Adapter(
            genreAdapter = listOfStringsAdapter,
            update_strategyAdapter = updateStrategyAdapter,
        ),
    )

    @Provides
    @Singleton
    fun provideSqliteDriver(context: Context): SqlDriver = AndroidSqliteDriver(
        schema = Database.Schema,
        context = context,
        name = "tachiyomi.db",
        factory = if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Support database inspector in Android Studio
            FrameworkSQLiteOpenHelperFactory()
        } else {
            RequerySQLiteOpenHelperFactory()
        },
        callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                setPragma(db, "foreign_keys = ON")
                setPragma(db, "journal_mode = WAL")
                setPragma(db, "synchronous = NORMAL")
            }

            private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
                val cursor = db.query("PRAGMA $pragma")
                cursor.moveToFirst()
                cursor.close()
            }
        },
    )

    @Provides
    @Singleton
    fun provideDatabaseHandler(db: Database, driver: SqlDriver): DatabaseHandler =
        AndroidDatabaseHandler(db = db, driver = driver)

}