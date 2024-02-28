package com.hika.myscent.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.hika.myscent.base.BaseRecyclerViewAdapter
import com.hika.myscent.common.toRupiahFormat
import com.hika.myscent.databinding.ItemCartBinding
import com.hika.myscent.model.Cart

class CartAdapter(
    private val onIncreaseAmount: (String, String, Int, String) -> Unit = { _, _, _, _ -> },
    private val onDecreaseAmount: (String) -> Unit = {},
    private val onDeleteItem: (String) -> Unit = {},
    private val onCheckedChange: (String, Boolean) -> Unit = {_, _ -> }
): BaseRecyclerViewAdapter<ItemCartBinding, Pair<Cart, Boolean>>() {

    override fun inflateViewBinding(parent: ViewGroup): ItemCartBinding {
        return ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<Pair<Cart, Boolean>>, List<Pair<Cart, Boolean>>) -> DiffUtil.Callback? = { _, _ -> null }

    override fun ItemCartBinding.binds(data: Pair<Cart, Boolean>) {
        val cart = data.first
        var amount = cart.amount
        tvName.text = cart.name
        tvPrice.text = cart.price.toRupiahFormat()
        includeCartCounter.tvAmount.text = cart.amount.toString()

        Glide.with(itemView.context)
            .load(cart.image)
            .into(ivPerfume)

        cbSelect.isChecked = data.second

        cbSelect.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(cart.productId, isChecked)
        }

        includeCartCounter.cvMinus.setOnClickListener {
            if (amount > 1) {
                onDecreaseAmount(cart.productId)
                amount -= 1
                includeCartCounter.tvAmount.text = cart.amount.toString()
            } else {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Delete Item?")
                    .setMessage("Are you sure you want to delete this item?")
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Yes") { dialog, _ ->
                        onDeleteItem(cart.productId)
                        dialog.dismiss()
                    }
                    .show()
            }

        }

        includeCartCounter.cvPlus.setOnClickListener {
            onIncreaseAmount(cart.productId, cart.name, cart.price, cart.image)
            amount += 1
            includeCartCounter.tvAmount.text = cart.amount.toString()
        }
    }
}