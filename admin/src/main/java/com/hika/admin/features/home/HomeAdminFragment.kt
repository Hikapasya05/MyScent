package com.hika.admin.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.admin.adapter.HomeAdminAdapter
import com.hika.admin.databinding.FragmentHomeAdminBinding
import com.hika.common.base.BaseFragment
import com.hika.common.common.invisible
import com.hika.common.common.visible
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.widget.queryTextChanges

class HomeAdminFragment : BaseFragment<FragmentHomeAdminBinding>() {

    private val viewModel by viewModel<HomeAdminViewModel>()

    private val homeAdminAdapter by lazy { HomeAdminAdapter(
        onItemPressed = {  }
    ) }

    override fun inflateViewBinding(container: ViewGroup?): FragmentHomeAdminBinding {
        return FragmentHomeAdminBinding.inflate(LayoutInflater.from(context), container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentHomeAdminBinding.bind() {
        viewModel.getPerfumes()

        rvHome.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvHome.adapter = homeAdminAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            svPerfume.queryTextChanges()
                .skipInitialValue()
                .debounce(200)
                .collect {
                    viewModel.searchPerfumes(it.toString())
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeState.collect {
                if (it.isLoading) {
                    pbHome.visible()
                    rvHome.invisible()
                } else {
                    pbHome.invisible()
                    rvHome.visible()
                }
                if (it.isError) showErrorSnackBar(it.errorMessage)
                if (it.isSuccess) homeAdminAdapter.submitList(it.successData.orEmpty())
            }
        }
    }
}