package com.globe.data.datasource.local.instrumentation

import android.annotation.SuppressLint
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.*

class RealmManager(
    private val keyProvider: RealmKeyProvider,
    private val context: Context
) {

    companion object {
        private const val REALM_DB_FILE_EXTENSION = ".realm"
        private const val REALM_PARENT_DIRECTORY = "realm/"

        private const val SHARED_PREF_NAME = "database_name_shared_prefs"
        private const val DB_NAME_KEY = "database_name_key"
    }

    val dispatcher: CoroutineDispatcher = Dispatchers.Main

    private lateinit var config: RealmConfiguration
    private val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    init {
        Realm.init(context)
        initializeDatabase()
    }

    @SuppressLint("CommitPrefEdits")
    private fun initializeDatabase() {
        var dbName = sharedPref.getString(DB_NAME_KEY, null)
        if (dbName == null) {
            dbName = UUID.randomUUID().toString()
            sharedPref.edit().putString(DB_NAME_KEY, dbName).apply()
        }
        val directory = REALM_PARENT_DIRECTORY + dbName
        config = createRealmConfig(directory, dbName)
    }

    private fun createRealmConfig(directory: String, realmDBName: String): RealmConfiguration {
        val encryptKey = keyProvider.readKey
        return RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .encryptionKey(encryptKey)
            .directory(File(context.filesDir, directory))
            .name(realmDBName + REALM_DB_FILE_EXTENSION)
            .build()
    }

    fun getRealmInstance(): Realm =
        Realm.getInstance(config)
}
