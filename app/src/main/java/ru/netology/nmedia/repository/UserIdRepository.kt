package ru.netology.nmedia.repository

import android.content.Context

interface UserIdRepository {
    var userId: Long

    fun clearPrefs()
}

class UserIdRepositoryPreferences(context: Context) : UserIdRepository {

    private val prefs = context.getSharedPreferences("userId", Context.MODE_PRIVATE)

    companion object {
        private const val idKey = "id"
    }

    override var userId: Long
        get() = prefs.getLong(idKey, 0L)
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