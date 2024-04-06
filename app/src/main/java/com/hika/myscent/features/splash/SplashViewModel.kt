package com.hika.myscent.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import com.hika.data.util.AuthorizeStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _authorizedUser = MutableStateFlow<AuthorizeStatus?>(null)
    val authorizedUser = _authorizedUser.asStateFlow()

    fun checkUserAuthorization() {
        viewModelScope.launch {
            userRepository.getAuthorizedUser().let {
                _authorizedUser.value = it
            }
        }
    }


}