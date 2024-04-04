package com.hika.admin.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hika.admin.R

class ProfileAdminFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileAdminFragment()
    }

    private lateinit var viewModel: ProfileAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}