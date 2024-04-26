package com.hika.auth.navigation

import android.app.Activity
import android.content.Intent
import com.hika.auth.features.auth.AuthActivity

abstract class AuthNavigation {

    fun navigateToAuth(activity: Activity) {
        val intent = Intent(activity, AuthActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}