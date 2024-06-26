package com.hika.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.data.model.PaymentMethod
import com.hika.user.R
import com.hika.user.databinding.ItemListPaymentMethodBinding

class PaymentMethodAdapter(
    private val onPaymentMethodSelected: (PaymentMethod?) -> Unit
): BaseRecyclerViewAdapter<ItemListPaymentMethodBinding, PaymentMethod>() {

    private var paymentMethod: Pair<Int, PaymentMethod>? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun inflateViewBinding(parent: ViewGroup): ItemListPaymentMethodBinding =
        ItemListPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override val diffUtilBuilder: (List<PaymentMethod>, List<PaymentMethod>) -> DiffUtil.Callback?
        get() = {_, _ -> null}

    override fun ItemListPaymentMethodBinding.bindsWithPosition(
        data: PaymentMethod,
        position: Int
    ) {
        ivPaymentMethod.setImageResource(data.image)
        if (paymentMethod != null) {
            if (selectedPosition == position) {
                paymentMethod = position to data
                containerPaymentMethod.setBackgroundResource(R.drawable.circle_white_selected)
            } else {
                containerPaymentMethod.setBackgroundResource(R.drawable.circle_white)
            }
        }
        root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            paymentMethod = position to data
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onPaymentMethodSelected(paymentMethod?.second)
        }
    }
}