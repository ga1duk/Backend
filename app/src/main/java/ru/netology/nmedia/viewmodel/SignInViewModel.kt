package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.database.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    fun updateUser(login: String, password: String) = viewModelScope.launch {
        try {
            val user = repository.updateUser(login, password)
            AppAuth.getInstance().setAuth(user.id, user.token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}