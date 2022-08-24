package ru.netology.nmedia.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKeyEntity(
    @PrimaryKey
    val key: KeyType,
    val id: Long
) {
    enum class KeyType {
        BEFORE,
        AFTER
    }
}