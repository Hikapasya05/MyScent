package com.hika.myscent.features.payment

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.myscent.adapter.PaymentMethodAdapter
import com.hika.myscent.base.BaseActivity
import com.hika.myscent.common.toRupiahFormat
import com.hika.myscent.databinding.ActivityPaymentBinding
import com.hika.myscent.model.Cart
import com.hika.myscent.util.IntentKeys.CHECKED_OUT_CARTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : BaseActivity<ActivityPaymentBinding>() {

    private val viewModel by viewModel<PaymentViewModel>()

    private val paymentMethodAdapter by lazy {
        PaymentMethodAdapter(
            onPaymentMethodSelected = {
                viewModel.setPaymentMethod(it)
            }
        )
    }

    override fun inflateViewBinding(): ActivityPaymentBinding {
        return ActivityPaymentBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun ActivityPaymentBinding.bind() {

        val checkedOutCarts: List<Cart> =
            intent.getParcelableArrayListExtra(CHECKED_OUT_CARTS, Cart::class.java)?.toList() ?: emptyList()

        rvPaymentMethod.apply {
            adapter = paymentMethodAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        btnConfirmPayment.setOnClickListener { viewModel.postHistory() }

        viewModel.getUser()
        viewModel.setCheckedOutCarts(checkedOutCarts)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.user.collect {
                tvDeliveryPointer.text = it?.address.orEmpty()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isError) showErrorSnackBar(it.errorMessage)
                if (it.isSuccess) {
                    val intent = Intent(this@PaymentActivity, PaymentVerificationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        viewModel.carts.observe(this@PaymentActivity) {
            tvPrice.text = it.sumOf { cart -> cart.price * cart.amount }.toString()
        }

        viewModel.shippingCost.observe(this@PaymentActivity) {
            tvShippingCost.text = it.toString()
        }

        viewModel.admin.observe(this@PaymentActivity) {
            tvAdmin.text = it.toString()
        }

        viewModel.totalPrice.observe(this@PaymentActivity) {
            tvTotal.text = it.toRupiahFormat()
        }

        viewModel.paymentMethod.observe(this@PaymentActivity) {
            btnConfirmPayment.isEnabled = it != null
        }

        paymentMethodAdapter.submitList(viewModel.getPaymentMethods())

    }
}