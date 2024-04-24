package com.hika.admin.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileAdminViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileAdminState())
    val state = _state.asStateFlow()

    fun getUser() {
        _state.value = ProfileAdminState(isLoading = true)
        viewModelScope.launch {
            userRepository.getUser()
                .onSuccess {
                    _state.value = ProfileAdminState(isSuccess = true, successData = it)
                }
                .onFailure {
                    _state.value = ProfileAdminState(isError = true, errorMessage = it.message.orEmpty())
                }
        }
    }

}