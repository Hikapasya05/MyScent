package com.hika.myscent.features.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hika.myscent.R
import com.hika.myscent.base.BaseActivity
import com.hika.myscent.databinding.ActivityPaymentVerificationBinding

class PaymentVerificationActivity : BaseActivity<ActivityPaymentVerificationBinding>() {
    override fun inflateViewBinding(): ActivityPaymentVerificationBinding {
        return ActivityPaymentVerificationBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation? {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityPaymentVerificationBinding.bind() {
        btnConfirm.setOnClickListener { finish() }
    }
}