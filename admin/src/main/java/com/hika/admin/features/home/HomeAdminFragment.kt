package com.hika.admin.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hika.admin.R

class HomeAdminFragment : Fragment() {

    companion object {
        fun newInstance() = HomeAdminFragment()
    }

    private lateinit var viewModel: HomeAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_admin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}