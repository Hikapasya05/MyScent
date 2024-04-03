package com.hika.myscent.features.cart

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseFragment
import com.hika.common.widget.CartSnackbar
import com.hika.myscent.adapter.CartAdapter
import com.hika.myscent.databinding.FragmentCartBinding
import com.hika.myscent.features.payment.PaymentActivity
import com.hika.myscent.util.IntentKeys
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding>() {

    private val viewModel by viewModel<CartViewModel>()

    override fun inflateViewBinding(container: ViewGroup?): FragmentCartBinding {
        return FragmentCartBinding.inflate(layoutInflater, container, false)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun FragmentCartBinding.bind() {
        viewModel.getCarts()

        val cartSnackbar = CartSnackbar.make(
            llCart,
            onSnackbarPressed = {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putParcelableArrayListExtra(
                    IntentKeys.CHECKED_OUT_CARTS,
                    viewModel.carts.value?.filter { it.second }?.map { it.first }?.toList()
                        ?.let { ArrayList(it) }
                )
                startActivity(intent)
            }
        )

        val cartAdapter = CartAdapter(
            onIncreaseAmount = { productId, name, price, image -> viewModel.addItem(productId, name, price, image) },
            onDecreaseAmount = { viewModel.removeItem(it) },
            onDeleteItem = { viewModel.deleteCart(it) },
            onCheckedChange = { productId, isChecked -> viewModel.onCheckedChange(productId, isChecked) }
        )

        rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        cbSelectAll.setOnCheckedChangeListener { _, isChecked -> viewModel.onSelectAll(isChecked) }

        viewModel.carts.observe(viewLifecycleOwner) {
            cartAdapter.submitList(it)

            val atLeastHaveOneChecked = it.any { it.second }
            if (atLeastHaveOneChecked) {
                val totalPrice = it.filter { it.second }.sumOf { it.first.price * it.first.amount }
                cartSnackbar.setPrice(totalPrice)
                cartSnackbar.itemsCount(it.filter { it.second}.sumOf { it.first.amount })
                cartSnackbar.show()
            } else {
                cartSnackbar.dismiss()
            }
        }
    }

}