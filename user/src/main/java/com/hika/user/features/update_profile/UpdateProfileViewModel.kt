package com.hika.user.features.update_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import com.hika.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().onSuccess {
                _user.value = it
            }
        }
    }

    fun updateUser(
        name: String,
        email: String,
        phone: String,
        address: String
    ) {
        viewModelScope.launch {
            val user = _user.value ?: return@launch
            val updatedUser = user.copy(
                username = name,
                email = email,
                phoneNumber = phone,
                address = address
            )
            userRepository.updateUser(updatedUser)
        }
    }

}
