package ru.netology.nmedia.repository

import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.database.dao.PostDao
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.LoginOrPassError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: ApiService
    ) : UserRepository {
    override suspend fun updateUser(login: String, password: String): User {
        try {
            val response = apiService.updateUser(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: ApiError) {
            throw when (e.status) {
                400, 404 -> LoginOrPassError
                else -> UnknownError
            }
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun createUser(name: String, login: String, password: String): User {
        try {
            val response = apiService.createUser(login, password, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: ApiError) {
            throw when (e.status) {
                403 -> LoginOrPassError
                else -> UnknownError
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
