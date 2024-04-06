package com.hika.myscent.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.hika.admin.features.AdminActivity
import com.hika.common.base.BaseActivity
import com.hika.data.util.AuthorizeStatus
import com.hika.myscent.databinding.ActivitySplashBinding
import com.hika.myscent.features.MainActivity
import com.hika.myscent.features.auth.AuthActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel by viewModel<SplashViewModel>()
    override fun inflateViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation? {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivitySplashBinding.bind() {
        viewModel.checkUserAuthorization()

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.authorizedUser.collect { user ->
                if (user == null) return@collect

                val intent = when (user) {
                    AuthorizeStatus.USER -> Intent(this@SplashActivity, MainActivity::class.java)
                    AuthorizeStatus.ADMIN -> Intent(this@SplashActivity, AdminActivity::class.java)
                    AuthorizeStatus.GUEST -> Intent(this@SplashActivity, AuthActivity::class.java)
                }

                startActivity(intent)
                finish()
            }
        }
    }
}