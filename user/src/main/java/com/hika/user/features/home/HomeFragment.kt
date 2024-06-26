package com.hika.user.features.home

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseFragment
import com.hika.common.common.invisible
import com.hika.common.common.visible
import com.hika.user.adapter.HomeAdapter
import com.hika.user.adapter.SearchAdapter
import com.hika.user.databinding.FragmentHomeBinding
import com.hika.user.features.product.ProductActivity
import com.hika.user.util.IntentKeys
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.widget.queryTextChanges

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModel<HomeViewModel>()

    private val homeAdapter by lazy { HomeAdapter(
        onItemPressed = { id -> onItemPressed(id) }
    ) }

    private val searchAdapter by lazy { SearchAdapter(
        onItemPressed = { onItemPressed(it.id) }
    ) }

    override fun inflateViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    @FlowPreview
    override fun FragmentHomeBinding.bind() {

        viewModel.getPerfumes()

        rvHome.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            svPerfume.queryTextChanges()
                .skipInitialValue()
                .debounce(200)
                .collect {
                    viewModel.setQuery(it.toString())
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

                when(it.listType) {
                    ListType.HOME -> {
                        rvHome.adapter = homeAdapter
                        homeAdapter.submitList(it.homePerfumes)
                    }
                    ListType.SEARCH -> {
                        rvHome.adapter = searchAdapter
                        searchAdapter.submitList(it.searchedPerfumes)
                    }
                }
            }
        }
    }

    private fun onItemPressed(id: String) {
        val intent = Intent(requireContext(), ProductActivity::class.java)
        intent.putExtra(IntentKeys.PERFUME_ID, id)
        startActivity(intent)
    }
}