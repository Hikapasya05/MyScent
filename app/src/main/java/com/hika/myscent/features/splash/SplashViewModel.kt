package com.hika.myscent.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            userRepository.isUserLoggedIn().let {
                _isLoggedIn.value = it
            }
        }
    }


}