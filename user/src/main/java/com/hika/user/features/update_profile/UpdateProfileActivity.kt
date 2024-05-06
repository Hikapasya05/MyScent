package com.hika.user.features.update_profile

import androidx.lifecycle.lifecycleScope
import com.hika.common.base.BaseActivity
import com.hika.user.databinding.ActivityUpdateProfileBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfileActivity : BaseActivity<ActivityUpdateProfileBinding>() {

    private val viewModel by viewModel<UpdateProfileViewModel>()

    override fun inflateViewBinding(): ActivityUpdateProfileBinding {
        return ActivityUpdateProfileBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityUpdateProfileBinding.bind() {
        viewModel.getUser()

        lifecycleScope.launch {
            viewModel.user.collect {
                it?.let {
                    etName.setText(it.username)
                    etEmail.setText(it.email)
                    etPhone.setText(it.phoneNumber)
                    etAddress.setText(it.address)
                }
            }
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val address = etAddress.text.toString()
            viewModel.updateUser(name, email, phone, address)
            finish()
        }
    }
}