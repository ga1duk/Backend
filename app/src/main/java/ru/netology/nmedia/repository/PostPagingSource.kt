package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.error.ApiError

class PostPagingSource(
    private val apiService: ApiService
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val response = when (params) {
                is LoadParams.Refresh -> apiService.getLatest(params.loadSize)
                is LoadParams.Append -> apiService.getBefore(params.key, params.loadSize)
                is LoadParams.Prepend -> return LoadResult.Page(emptyList(), params.key, null)
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())

            val nextKey = if (body.isEmpty()) null else body.last().id

            return LoadResult.Page(body, params.key, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}