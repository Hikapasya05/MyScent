package com.hika.myscent.features.history.payment

import androidx.lifecycle.lifecycleScope
import com.hika.common.base.BaseActivity
import com.hika.common.common.toRupiahFormat
import com.hika.myscent.databinding.ActivityHistoryPaymentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryPaymentActivity : BaseActivity<ActivityHistoryPaymentBinding>() {

    private val viewModel by viewModel<HistoryPaymentViewModel>()

    override fun inflateViewBinding(): ActivityHistoryPaymentBinding {
        return ActivityHistoryPaymentBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityHistoryPaymentBinding.bind() {

        val historyId = intent.getStringExtra(HISTORY_ID).orEmpty()
        viewModel.getHistoryById(historyId)

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isError) showErrorSnackBar(it.errorMessage)
                if (it.isSuccess) {
                    tvDeliveryPointer.text = it.successData?.shippingAddress
                    tvTotalPrice.text = it.successData?.totalPrice?.toRupiahFormat()
                    btnConfirmPayment.setOnClickListener {
                        viewModel.confirmPayment(historyId) {
                            showSuccessSnackBar("Payment confirmation sent. Please wait for admin approval")
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val HISTORY_ID = "history"
    }
}