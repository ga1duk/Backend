package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.database.db.AppDb
import ru.netology.nmedia.error.LoginOrPassError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.model.SignInModelState
import ru.netology.nmedia.model.SignUpModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.repository.UserRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository =
        UserRepositoryImpl(AppDb.getInstance(context = application).postDao())

    private val _dataState = SingleLiveEvent<SignUpModelState>()
    val dataState: LiveData<SignUpModelState>
        get() = _dataState

    fun createUser(login: String, password: String, name: String) = viewModelScope.launch {
        try {
            if (login != "" && password != "" && name != "") {
                val user = repository.createUser(login, password, name)
                AppAuth.getInstance().setAuth(user.id, user.token)
                _dataState.value = SignUpModelState()
            } else {
                _dataState.value = SignUpModelState(emptyFieldsError = true)
            }
        } catch (e: LoginOrPassError) {
            _dataState.value = SignUpModelState(loginOrPassError = true)
        } catch (e: NetworkError) {
            _dataState.value = SignUpModelState(networkError = true)
        } catch (e: Exception) {
            _dataState.value = SignUpModelState(unknownError = true)
        }
    }
}