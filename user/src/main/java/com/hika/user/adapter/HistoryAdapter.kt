package com.hika.user.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.common.common.gone
import com.hika.common.common.toOrderHistoryFormat
import com.hika.common.common.toRupiahFormat
import com.hika.common.common.visible
import com.hika.common.databinding.ItemHistoryBinding
import com.hika.common.databinding.ItemProductOrderBinding
import com.hika.common.util.OrderStatus
import com.hika.common.widget.buildOrderHistoryConfirmationDialog
import com.hika.data.model.History
import com.hika.data.model.PerfumeHistory

class HistoryAdapter(
    private val onPositiveButtonClick: (String, String) -> Unit = { _, _ -> },
    private val onNegativeButtonClick: Dialog.(String, String) -> Unit = {_, _ ->  }
): BaseRecyclerViewAdapter<ItemHistoryBinding, History>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemHistoryBinding {
        return ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<History>, List<History>) -> DiffUtil.Callback?
        get() = { _, _ -> null }

    override fun ItemHistoryBinding.binds(data: History) {
        tvBuyer.text = "Your Order"
        tvDate.text = data.date.toOrderHistoryFormat()

        val productOrderAdapter = com.hika.user.adapter.ProductHistoryAdapter()
        rvOrder.apply {
            adapter = productOrderAdapter
            layoutManager = LinearLayoutManager(root.context)
        }
        productOrderAdapter.submitList(data.products)

        tvTotal.text = data.totalPrice.toRupiahFormat()
        tvShippingAddressValue.text = data.shippingAddress

        btnPositive.text = "Confirm"
        btnNegative.text = "Cancel"

        when(data.status) {
            OrderStatus.WAIT_FOR_ADMIN_CONFIRMATION.name -> {
                tvStatus.text = "Waiting for admin confirmation"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.warning_500))
                llButtons.gone()
            }
            OrderStatus.WAIT_FOR_USER_PAYMENT.name -> {
                tvStatus.text = "Waiting for your payment"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.warning_500))
            }
            OrderStatus.WAIT_FOR_ADMIN_PAYMENT_APPROVAL.name -> {
                tvStatus.text = "Waiting for admin payment approval"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.warning_500))
                llButtons.gone()
            }
            OrderStatus.SHIPPING.name -> {
                tvStatus.text = "Shipping"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.success_500))
                llButtons.gone()
            }
            OrderStatus.REJECTED_BY_ADMIN.name -> {
                tvStatus.text = "Rejected by admin"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.error_500))
                llButtons.gone()
            }
            OrderStatus.REJECTED_BY_USER.name -> {
                tvStatus.text = "Rejected by you"
                tvStatus.setTextColor(root.context.getColor(com.hika.common.R.color.error_500))
                llButtons.gone()
            }
        }

        btnPositive.setOnClickListener {
            onPositiveButtonClick(data.id, data.status)
        }

        btnNegative.setOnClickListener {
            root.context.buildOrderHistoryConfirmationDialog(
                title = "Cancel Order",
                message = "Please enter the reason for cancellation",
                showTextField = true,
            ) {
                this.onNegativeButtonClick(data.id, it)
            }.show()
        }

        if (data.reason != null) {
            tvAdditionalInformation.text = "Reason: ${data.reason}"
            tvAdditionalInformation.visible()
        }
    }
}

class ProductHistoryAdapter: BaseRecyclerViewAdapter<ItemProductOrderBinding, PerfumeHistory>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemProductOrderBinding {
        return ItemProductOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<PerfumeHistory>, List<PerfumeHistory>) -> DiffUtil.Callback?
        get() = { _, _ -> null }

    override fun ItemProductOrderBinding.binds(data: PerfumeHistory) {
        tvProductName.text = data.name
        tvQuantity.text = "Quantity: ${data.amount}"
        tvTotalPrice.text = data.totalPrice.toRupiahFormat()
    }

}