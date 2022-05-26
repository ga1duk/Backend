package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.database.dao.PostDao
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.error.LoginOrPassError
import java.io.IOException

class UserRepositoryImpl(private val dao: PostDao) : UserRepository {

    override suspend fun updateUser(login: String, password: String): User {
        try {
            val response = PostApi.retrofitService.updateUser(login, password)
            if (!response.isSuccessful) {
                if (response.code() == 404 || response.code() == 400) {
                    throw LoginOrPassError
                } else throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: LoginOrPassError) {
            throw e
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun createUser(login: String, password: String, name: String): User {
        try {
            val response = PostApi.retrofitService.createUser(login, password, name)
            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw LoginOrPassError
                }
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: LoginOrPassError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
