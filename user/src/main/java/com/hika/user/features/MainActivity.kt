package com.hika.user.features

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hika.common.base.BaseActivity
import com.hika.user.R
import com.hika.user.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun inflateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityMainBinding.bind() {
        val navController = findNavController(R.id.main_navigation)
        binding.mainBottomNav.setupWithNavController(navController)
    }
}