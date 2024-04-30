package com.hika.myscentapp.navigator

import android.app.Activity
import com.hika.auth.navigation.AuthNavigation
import com.hika.user.navigation.UserNavigation

class UserNavigator(
    private val authNavigation: AuthNavigation
): UserNavigation() {

    override fun navigateToAuth(activity: Activity) {
        authNavigation.navigateToAuth(activity)
    }
}