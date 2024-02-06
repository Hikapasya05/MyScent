package com.hika.myscent.features.home

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.myscent.adapter.PerfumeAdapter
import com.hika.myscent.base.BaseFragment
import com.hika.myscent.databinding.FragmentHomeBinding
import com.hika.myscent.features.product.ProductActivity
import com.hika.myscent.model.Perfume
import com.hika.myscent.util.IntentKeys
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModel<HomeViewModel>()

    private val womenCategoryAdapter by lazy { PerfumeAdapter { onItemPressed(it) } }
    private val menCategoryAdapter by lazy { PerfumeAdapter { onItemPressed(it) } }
    private val unisexCategoryAdapter by lazy { PerfumeAdapter { onItemPressed(it) } }

    override fun inflateViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentHomeBinding.bind() {

        viewModel.getPerfumes()

        rvMen.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = menCategoryAdapter
        }

        rvWomen.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = womenCategoryAdapter
        }

        rvUnisex.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = unisexCategoryAdapter
        }

        lifecycleScope.launch {
            viewModel.homeState.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isSuccess) {
                    val men = it.successData?.get("Men").orEmpty()
                    val women = it.successData?.get("Women").orEmpty()
                    val unisex = it.successData?.get("Unisex").orEmpty()

                    menCategoryAdapter.submitList(men)
                    womenCategoryAdapter.submitList(women)
                    unisexCategoryAdapter.submitList(unisex)
                }
                if (it.isError) showErrorSnackBar(it.errorMessage)
            }
        }
    }

    private fun onItemPressed(perfume: Perfume) {
        val intent = Intent(requireContext(), ProductActivity::class.java)
        intent.putExtra(IntentKeys.PERFUME_ID, perfume.id)
        startActivity(intent)
    }
}