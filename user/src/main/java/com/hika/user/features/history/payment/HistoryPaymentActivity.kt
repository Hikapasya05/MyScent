package com.hika.user.features.history.payment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.hika.common.base.BaseActivity
import com.hika.common.common.toRupiahFormat
import com.hika.common.common.visible
import com.hika.common.widget.buildOrderHistoryConfirmationDialog
import com.hika.user.databinding.ActivityHistoryPaymentBinding
import com.hika.user.util.CameraManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryPaymentActivity : BaseActivity<ActivityHistoryPaymentBinding>() {

    private val viewModel by viewModel<HistoryPaymentViewModel>()

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraManager = CameraManager(this@HistoryPaymentActivity) { viewModel.addPhoto(it) }
    }

    override fun inflateViewBinding(): ActivityHistoryPaymentBinding {
        return ActivityHistoryPaymentBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityHistoryPaymentBinding.bind() {

        val historyId = intent.getStringExtra(HISTORY_ID).orEmpty()
        viewModel.getHistoryById(historyId)

        cvTakePicture.setOnClickListener {
            cameraManager.openCamera()
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isError) showErrorSnackBar(it.errorMessage)
                if (it.isSuccess) {
                    tvDeliveryPointer.text = it.successData?.shippingAddress
                    tvTotalPrice.text = it.successData?.totalPrice?.toRupiahFormat()
                    btnConfirmPayment.setOnClickListener {
                        buildOrderHistoryConfirmationDialog(
                            "Payment Confirmation",
                            "Before you confirm, please make sure you have transferred the payment to the account number"
                        ) {
                            viewModel.confirmPayment(historyId) {
                                finish()
                                showSuccessSnackBar("Payment confirmation sent. Please wait for admin approval")
                            }
                        }.show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.photo.collect {
                if (it != null) ivPaymentRecipt.setImageBitmap(it)
                if (it != null) ivPaymentRecipt.visible()

                btnConfirmPayment.isEnabled = it != null
            }
        }
    }

    companion object {
        const val HISTORY_ID = "history"
    }
}