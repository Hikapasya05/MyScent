package com.hika.user.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import com.hika.user.R
import com.hika.user.util.ProfileSetting
import com.hika.user.util.ProfileSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _settings = MutableStateFlow(emptyList<ProfileSettings>())
    val settings = _settings.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    init {
        _settings.value = listOf(
            ProfileSettings(ProfileSetting.UPDATE_PROFILE, "Update Profile", R.drawable.ic_profile_update_profile),
            ProfileSettings(ProfileSetting.ORDER_HISTORY, "Order History", R.drawable.ic_profile_history)
        )
    }

    fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().onSuccess {
                _username.value = it.username
            }
        }
    }

}