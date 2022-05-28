package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.User

interface UserRepository {
    suspend fun updateUser(login: String, password: String): User
    suspend fun createUser(name: String, login: String, password: String): User
}
