package com.hika.user.features.profile

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseFragment
import com.hika.user.adapter.ProfileSettingsAdapter
import com.hika.user.databinding.FragmentProfileBinding
import com.hika.user.features.history.HistoryActivity
import com.hika.user.features.update_profile.UpdateProfileActivity
import com.hika.user.navigation.UserNavigation
import com.hika.user.util.ProfileSetting
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val profileSettingsAdapter by lazy {
        ProfileSettingsAdapter { setting ->
            when(setting) {
                ProfileSetting.UPDATE_PROFILE -> {
                    val intent = Intent(requireContext(), UpdateProfileActivity::class.java)
                    startActivity(intent)
                }
                ProfileSetting.ORDER_HISTORY -> {
                    val intent = Intent(requireContext(), HistoryActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private val viewModel by viewModel<ProfileViewModel>()

    private val userNavigation by inject<UserNavigation>()

    override fun inflateViewBinding(container: ViewGroup?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentProfileBinding.bind() {
        rvSettings.apply {
            adapter = profileSettingsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        btnLogout.setOnClickListener {
            userNavigation.navigateToAuth(requireActivity())
        }

        lifecycleScope.launch {
            viewModel.settings.collect {
                profileSettingsAdapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.username.collect {
                tvUsername.text = it
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }
}