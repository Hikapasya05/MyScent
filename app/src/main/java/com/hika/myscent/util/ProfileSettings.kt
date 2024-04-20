package com.hika.myscent.util

enum class ProfileSetting {
    UPDATE_PROFILE,
    ORDER_HISTORY,
}

data class ProfileSettings(
    val setting: ProfileSetting,
    val title: String,
    val icon: Int
)
