package com.hika.myscent.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hika.myscent.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState?>(null)
    val loginState = _loginState.asStateFlow().asLiveData().asFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            authRepository.login(email, password).let { result ->
                if (result.isSuccess) {
                    _loginState.value = LoginState(isSuccess = true, successData = Unit)
                } else {
                    _loginState.value = LoginState(isError = true, errorMessage = result.exceptionOrNull()?.message.orEmpty())
                }
            }
        }
    }

}