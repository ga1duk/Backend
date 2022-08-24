package ru.netology.nmedia.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.database.dao.PostDao
import ru.netology.nmedia.database.dao.PostRemoteKeyDao
import ru.netology.nmedia.database.db.AppDb
import ru.netology.nmedia.database.entity.PostEntity
import ru.netology.nmedia.database.entity.PostRemoteKeyEntity
import ru.netology.nmedia.database.entity.toEntity
import ru.netology.nmedia.error.ApiError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> apiService.getLatest(state.config.initialLoadSize)
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastId = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    apiService.getBefore(lastId, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())

            if (body.isEmpty()) {
                return MediatorResult.Success(false)
            }

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.AFTER,
                                body.first().id
                            )
                        )
                    }
                    LoadType.PREPEND -> {
                        return@withTransaction
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id
                            )
                        )
                    }
                }

                postDao.insert(body.toEntity())
            }
            return MediatorResult.Success(body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}