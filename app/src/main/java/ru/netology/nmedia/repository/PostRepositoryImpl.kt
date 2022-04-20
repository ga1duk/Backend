package ru.netology.nmedia.repository

import androidx.lifecycle.map
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.database.dao.PostDao
import ru.netology.nmedia.database.entity.PostEntity
import ru.netology.nmedia.database.entity.toDto
import ru.netology.nmedia.database.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toDto)

    override suspend fun getAll() {
        try {
            val response = PostApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            val response = PostApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.likeById(body.id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long) {
        try {
            val response = PostApi.retrofitService.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.likeById(body.id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


//    override suspend fun getAll(callback: PostRepository.GetAllCallback) {
//        PostApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (!response.isSuccessful) {
//                    callback.onError(RuntimeException(response.message()))
//                    return
//                } else {
//                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
//                }
//            }
//
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                callback.onError(t as Exception)
//            }
//        })
//    }


    //    override suspend fun likeById(id: Long, callback: PostRepository.LikeCallback) {
//        PostApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                if (!response.isSuccessful) {
//                    callback.onError(RuntimeException(response.message()))
//                    return
//                } else {
//                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
//                }
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//                callback.onError(t as Exception)
//            }
//        })
//    }
//
//    override suspend fun dislikeById(id: Long, callback: PostRepository.LikeCallback) {
//        PostApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                if (!response.isSuccessful) {
//                    callback.onError(RuntimeException(response.message()))
//                    return
//                } else {
//                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
//                }
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//                callback.onError(t as Exception)
//            }
//        })
//    }
//


//    override suspend fun save(post: Post) {
//        try {
//            val response = PostApi.retrofitService.save(post)
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }


//    override suspend fun removeById(id: Long) {
//        TODO("Not yet implemented")
//    }


//    override suspend fun save(post: Post, callback: PostRepository.SaveCallback) {
//        PostApi.retrofitService.save(post).enqueue(object : Callback<Post> {
//            override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                if (!response.isSuccessful) {
//                    callback.onError(RuntimeException(response.message()))
//                    return
//                } else {
//                    callback.onSuccess()
//                }
//            }
//
//            override fun onFailure(call: Call<Post>, t: Throwable) {
//                callback.onError(t as Exception)
//            }
//        })
//    }
//
//    override suspend fun removeById(id: Long, callback: PostRepository.DeleteCallback) {
//        PostApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
//            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                if (!response.isSuccessful) {
//                    callback.onError(RuntimeException(response.message()))
//                } else {
//                    callback.onSuccess()
//                }
//            }
//
//            override fun onFailure(call: Call<Unit>, t: Throwable) {
//                callback.onError(t as Exception)
//            }
//        })
//    }
//}
}
