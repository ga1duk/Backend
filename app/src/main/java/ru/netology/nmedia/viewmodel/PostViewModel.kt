package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

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
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun refreshPosts() {
        _data.value = FeedModel(refreshing = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun save() {
        edited.value?.let {
                repository.saveAsync(it, object: PostRepository.SaveCallback {
                    override fun onSuccess() {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
        }
        edited.value = empty
    }

//    fun save() {
//        edited.value?.let {
//            thread {
//                repository.save(it)
//                _postCreated.postValue(Unit)
//            }
//        }
//        edited.value = empty
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

    fun likeById(id: Long) {
        repository.likeByIdAsync(id, object : PostRepository.LikeCallback {
            override fun onSuccess(post: Post) {
                val posts = _data.value?.posts.orEmpty()
                    .map {
                        if (it.id == post.id) post else it
                    }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun dislikeById(id: Long) {
        repository.dislikeByIdAsync(id, object: PostRepository.LikeCallback {
            override fun onSuccess(post: Post) {
                val posts = _data.value?.posts.orEmpty()
                    .map {
                        if (it.id == post.id) post else it
                    }
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Long) {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            val posts = old.filter { it.id != id }
            _data.postValue(
                FeedModel(posts = posts, empty = posts.isEmpty())
            )
                repository.removeByIdAsync(id, object: PostRepository.DeleteCallback {
                    override fun onSuccess() {
                    }
                    override fun onError(e: Exception) {
                        _data.postValue(_data.value?.copy(posts = old))
                    }
                })
        }
    }
