package com.ucsdextandroid2.petfinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by rjaylward on 2019-07-12
 */

class AccessTokenCache private constructor(context: Context) {

    private val sharedPrefs: SharedPreferences = context
        .getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE)

    fun updateToken(formattedToken: String?, expiration: Long) {
        sharedPrefs.edit {
            putString(TOKEN, formattedToken)
        }

        sharedPrefs.edit {
            putLong(TOKEN_EXPIRATION, expiration)
        }
    }

    fun getCachedToken(): String? {
        val token: String? = sharedPrefs.getString(TOKEN, null)
        if(token != null && System.currentTimeMillis() < sharedPrefs.getLong(TOKEN_EXPIRATION, 0))
            return token

        return null
    }

    companion object {

        private const val AUTH_PREFS_NAME = "pet_search_auth_prefs"
        private const val TOKEN = "token"
        private const val TOKEN_EXPIRATION = "token_expiration"

        @SuppressLint("StaticFieldLeak")
        private var accessTokenCacheInstance: AccessTokenCache? = null

        val instance: AccessTokenCache
            get() = accessTokenCacheInstance!!

        fun init(context: Context) {
            accessTokenCacheInstance = AccessTokenCache(context)
        }

    }

}