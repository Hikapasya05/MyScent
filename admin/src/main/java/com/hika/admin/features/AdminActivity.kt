package com.hika.admin.features

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hika.admin.R
import com.hika.admin.databinding.ActivityAdminBinding
import com.hika.common.base.BaseActivity

class AdminActivity : BaseActivity<ActivityAdminBinding>() {
    override fun inflateViewBinding(): ActivityAdminBinding {
        return ActivityAdminBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityAdminBinding.bind() {
        val navController = findNavController(R.id.admin_navigation)
        binding.mainBottomNav.setupWithNavController(navController)
    }
}