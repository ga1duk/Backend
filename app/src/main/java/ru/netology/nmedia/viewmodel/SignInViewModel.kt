package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.LoginOrPassError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.model.SignInModelState
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appAuth: AppAuth
) : ViewModel() {

    private val _dataState = SingleLiveEvent<SignInModelState>()
    val dataState: LiveData<SignInModelState>
        get() = _dataState

    fun updateUser(login: String, password: String) = viewModelScope.launch {
        try {
            if (login != "" && password != "") {
                val user = userRepository.updateUser(login, password)
                appAuth.setAuth(user.id, user.token)
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