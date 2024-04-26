package com.hika.auth.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.auth.AuthRepository
import com.hika.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState?>(null)
    val registerState = _registerState.asStateFlow()

    fun register(user: User, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)
            authRepository.register(user, password).let { result ->
                if (result.isSuccess) {
                    _registerState.value = RegisterState(isSuccess = true, successData = Unit)
                } else {
                    _registerState.value = RegisterState(isError = true, errorMessage = result.exceptionOrNull()?.message.orEmpty())
                }
            }
        }
    }

}