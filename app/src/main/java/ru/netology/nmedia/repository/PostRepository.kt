package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.User

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun removeById(id: Long)
    fun getNewerPostsCount(id: Long): Flow<Int>
    suspend fun setAllPostsVisibilityToTrue()
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun updateUser(login: String, password: String): User
}
