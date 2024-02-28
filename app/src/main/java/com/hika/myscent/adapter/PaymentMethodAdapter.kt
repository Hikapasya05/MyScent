package com.hika.myscent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hika.myscent.R
import com.hika.myscent.base.BaseRecyclerViewAdapter
import com.hika.myscent.databinding.ItemListPaymentMethodBinding
import com.hika.myscent.model.PaymentMethod

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