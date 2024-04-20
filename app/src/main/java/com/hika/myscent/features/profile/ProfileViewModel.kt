package com.hika.myscent.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.user.UserRepository
import com.hika.myscent.R
import com.hika.myscent.util.ProfileSetting
import com.hika.myscent.util.ProfileSettings
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
            ProfileSettings(ProfileSetting.UPDATE_PROFILE, "Change Profile", R.drawable.ic_profile_update_profile),
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