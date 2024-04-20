package com.hika.admin.features.order

import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.admin.adapter.OrderAdminAdapter
import com.hika.admin.databinding.FragmentOrderAdminBinding
import com.hika.common.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderAdminFragment : BaseFragment<FragmentOrderAdminBinding>() {

    private val viewModel by viewModel<OrderAdminViewModel>()

    private val orderAdminAdapter by lazy {
        OrderAdminAdapter(
            onPositiveButtonClick = { orderId, status -> viewModel.confirmOrder(orderId, status) },
            onNegativeButtonClick = { orderId -> viewModel.rejectOrder(orderId) }
        )
    }

    override fun inflateViewBinding(container: ViewGroup?): FragmentOrderAdminBinding {
        return FragmentOrderAdminBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentOrderAdminBinding.bind() {

        rvOrder.apply {
            adapter = orderAdminAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getOrders()

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isSuccess) orderAdminAdapter.submitList(it.successData)
                if (it.isError) showErrorSnackBar(it.errorMessage)
            }
        }
    }

}