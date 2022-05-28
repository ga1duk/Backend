package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.database.db.AppDb
import ru.netology.nmedia.error.LoginOrPassError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.model.SignInModelState
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.repository.UserRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository =
        UserRepositoryImpl(AppDb.getInstance(context = application).postDao())

    private val _dataState = SingleLiveEvent<SignInModelState>()
    val dataState: LiveData<SignInModelState>
        get() = _dataState

    fun updateUser(login: String, password: String) = viewModelScope.launch {
        try {
            if (login != "" && password != "") {
                val user = repository.updateUser(login, password)
                AppAuth.getInstance().setAuth(user.id, user.token)
                _dataState.value = SignInModelState()
            } else {
                _dataState.value = SignInModelState(emptyFieldsError = true)
            }
        } catch (e: LoginOrPassError) {
            _dataState.value = SignInModelState(loginOrPassError = true)
        } catch (e: NetworkError) {
            _dataState.value = SignInModelState(networkError = true)
        } catch (e: Exception) {
            _dataState.value = SignInModelState(unknownError = true)
        }
    }
}