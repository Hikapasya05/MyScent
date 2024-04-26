package com.hika.admin.features.profile

import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.hika.admin.databinding.FragmentProfileAdminBinding
import com.hika.admin.navigation.AdminNavigation
import com.hika.common.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileAdminFragment : BaseFragment<FragmentProfileAdminBinding>() {

    private val viewModel by viewModel<ProfileAdminViewModel>()

    private val adminNavigation by inject<AdminNavigation>()

    override fun inflateViewBinding(container: ViewGroup?): FragmentProfileAdminBinding {
        return FragmentProfileAdminBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation? {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentProfileAdminBinding.bind() {
        viewModel.getUser()

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isSuccess) tvUsername.text = it.successData?.username
                if (it.isError) showErrorSnackBar(it.errorMessage)
            }
        }

        btnLogout.setOnClickListener {
            adminNavigation.navigateToAuth(requireActivity())
        }
    }
}