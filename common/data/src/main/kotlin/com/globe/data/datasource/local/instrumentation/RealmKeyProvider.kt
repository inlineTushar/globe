package com.globe.data.datasource.local.instrumentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.SecureRandom

class RealmKeyProvider(context: Context) {

    companion object {
        private const val REALM_KEY = "realm_key"
        private const val SHARED_PREF_NAME = "secret_shared_prefs"
        private const val KEY_LENGTH = 64
    }

    private var masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        SHARED_PREF_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val readKey: ByteArray
        get() {
            val key = sharedPreferences.getString(REALM_KEY, "")?.encode() ?: ByteArray(0)
            return if (key.isEmpty()) {
                val newKey = generateSecureRandomKey()
                saveKey(newKey)
                newKey
            } else {
                key
            }
        }

    @SuppressLint("CommitPrefEdits")
    private fun saveKey(key: ByteArray) {
        sharedPreferences.edit().putString(REALM_KEY, key.decode()).apply()
    }

    private fun generateSecureRandomKey(): ByteArray {
        val key = ByteArray(KEY_LENGTH)
        SecureRandom().nextBytes(key)
        return key
    }

    private fun ByteArray.decode() = Base64.encodeToString(this, Base64.NO_WRAP)
    private fun String.encode() = Base64.decode(this, Base64.NO_WRAP)
}
