package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.database.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
    content = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }
//        repository.getAll(object : PostRepository.GetAllCallback {
//            override fun onSuccess(posts: List<Post>) {
//                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
//        })
//    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun dislikeById(id: Long) = viewModelScope.launch {
        try {
            repository.dislikeById(id)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

//        fun likeById(id: Long) {
//        repository.likeById(id, object : PostRepository.LikeCallback {
//            override fun onSuccess(post: Post) {
//                val posts = _data.value?.posts.orEmpty()
//                    .map {
//                        if (it.id == post.id) post else it
//                    }
//                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
//        })
//    }
//
//    fun dislikeById(id: Long) {
//        repository.dislikeById(id, object : PostRepository.LikeCallback {
//            override fun onSuccess(post: Post) {
//                val posts = _data.value?.posts.orEmpty()
//                    .map {
//                        if (it.id == post.id) post else it
//                    }
//                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(FeedModel(error = true))
//            }
//        })
//    }
//

//    fun save() {
//        edited.value?.let {
//            _postCreated.value = Unit
//            viewModelScope.launch {
//                try {
//                    repository.save(it)
//                    _dataState.value = FeedModelState()
//                } catch (e: Exception) {
//                    _dataState.value = FeedModelState(error = true)
//                }
//            }
//            edited.value = empty
//        }
//    }

fun edit(post: Post) {
    edited.value = post
}

fun changeContent(content: String) {
    val text = content.trim()
    if (edited.value?.content == text) {
        return
    }
    edited.value = edited.value?.copy(content = text)
}


//    fun removeById(id: Long) {
//        // Оптимистичная модель
//        val old = _data.value?.posts.orEmpty()
//        val posts = old.filter { it.id != id }
//        _data.postValue(
//            FeedModel(posts = posts, empty = posts.isEmpty())
//        )
//        repository.removeById(id, object : PostRepository.DeleteCallback {
//            override fun onSuccess() {
//            }
//
//            override fun onError(e: Exception) {
//                _data.postValue(_data.value?.copy(posts = old))
//            }
//        })
//    }
}
