package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.LoginOrPassError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.model.SignUpModelState
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appAuth: AppAuth
) : ViewModel() {

    private val _dataState = SingleLiveEvent<SignUpModelState>()
    val dataState: LiveData<SignUpModelState>
        get() = _dataState

    fun createUser(name: String, login: String, password: String, passwordConfirmation: String) =
        viewModelScope.launch {
            try {
                if (login != "" && password != "" && name != "") {
                    if (passwordConfirmation != password) {
                        _dataState.value = SignUpModelState(passwordsNotMatchError = true)
                    } else {
                        val user = userRepository.createUser(login, password, name)
                        appAuth.setAuth(user.id, user.token)
                        _dataState.value = SignUpModelState()
                    }
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