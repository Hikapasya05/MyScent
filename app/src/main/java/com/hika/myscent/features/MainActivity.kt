package com.hika.myscent.features

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hika.myscent.R
import com.hika.myscent.base.BaseActivity
import com.hika.myscent.databinding.ActivityMainBinding

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