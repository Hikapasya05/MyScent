package com.hika.myscentapp.navigator

import android.app.Activity
import com.hika.admin.navigation.AdminNavigation
import com.hika.auth.navigation.AuthNavigation

class AdminNavigator(
    private val authNavigation: AuthNavigation
): AdminNavigation() {
    override fun navigateToAuth(activity: Activity) {
        authNavigation.navigateToAuth(activity)
    }
}