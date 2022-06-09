package ru.netology.nmedia.repository

import android.content.Context

interface TokenRepository {
    var token: String?
    var userId: Long

    fun clearPrefs()
}

class TokenRepositoryPreferences(context: Context) : TokenRepository {

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    companion object {
        private const val idKey = "id"
        private const val tokenKey = "token"
    }

    override var token: String?
        get() = prefs.getString(tokenKey, null)
        set(value) {
            with(prefs.edit()) {
                putString(tokenKey, value)
                apply()
            }
        }
    override var userId: Long
        get() = prefs.getLong(idKey, 0)
        set(value) {
            with(prefs.edit()) {
                putLong(idKey, value)
                apply()
            }
        }

    override fun clearPrefs() {
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}