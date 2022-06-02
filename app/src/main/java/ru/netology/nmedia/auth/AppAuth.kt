package ru.netology.nmedia.auth

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val apiService: ApiService,
    private val tokenRepository: TokenRepository
) {

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = tokenRepository.userId
        val token = tokenRepository.token

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            tokenRepository.clearPrefs()
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }

        sendPushToken()
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = AuthState(id, token)
        tokenRepository.token = token
        tokenRepository.userId = id
        sendPushToken()
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        tokenRepository.clearPrefs()
        sendPushToken()
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                apiService.saveToken(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

data class AuthState(val id: Long = 0, val token: String? = null)