package com.ancraz.chatai.data

import android.content.Context
import android.content.SharedPreferences
import com.ancraz.chatai.common.utils.debugLog
import java.util.UUID

object AppPrefs {

    var sharedPrefs: SharedPreferences? = null
        private set

    private const val SHARED_PREFS_NAME = "AI_CHAT_PREFS"
    private const val USER_ID_KEY = "user_id"

    var userId: String
        get() {
            val oldUDID =
                get<String>(USER_ID_KEY)
            return if (oldUDID != "") {
                oldUDID
            } else {
                val newUdid = UUID.randomUUID().toString()
                userId = newUdid
                newUdid

            }
        }
        set(value) {
            put(USER_ID_KEY, value)
        }



    fun initPrefs(context: Context){
        sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }


    private fun put(key: String, value: Any) {
        sharedPrefs?.let {
            when (value) {
                is String -> it.edit().putString(key, value).apply()
                is Boolean -> it.edit().putBoolean(key, value).apply()
                is Long -> it.edit().putLong(key, value).apply()
                is Int -> it.edit().putInt(key, value).apply()
                else -> throw UnsupportedOperationException("unsupportedDataFormat")
            }
        }
    }

    inline fun <reified T : Any> get(key: String): T {
        return when (T::class) {
            String::class -> sharedPrefs?.getString(key, "") as T
            Boolean::class -> sharedPrefs?.getBoolean(key, false) as T
            Long::class -> sharedPrefs?.getLong(key, 0L) as T
            Int::class -> sharedPrefs?.getInt(key, 0) as T

            else -> throw UnsupportedOperationException("unsupportedDataFormat")
        }
    }


}