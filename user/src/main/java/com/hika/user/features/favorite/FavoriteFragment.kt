package com.hika.user.features.favorite

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseFragment
import com.hika.user.databinding.FragmentFavoriteBinding
import com.hika.user.features.product.ProductActivity
import com.hika.user.util.IntentKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private val viewModel by viewModel<FavoriteViewModel>()
    private val favoriteAdapter by lazy {
        com.hika.user.adapter.FavoriteAdapter(
            onItemPressed = {
                val intent = Intent(requireContext(), ProductActivity::class.java)
                intent.putExtra(IntentKeys.PERFUME_ID, it.id)
                startActivity(intent)
            },
            onFavoritePressed = { viewModel.deleteFavorite(it) }
        )
    }

    override fun inflateViewBinding(container: ViewGroup?): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentFavoriteBinding.bind() {
        viewModel.getFavorite()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.favoriteState.collect { state ->
                if (state.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (state.isSuccess) favoriteAdapter.submitList(state.successData)
                if (state.isError) showErrorSnackBar(state.errorMessage)
            }
        }

        rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


}