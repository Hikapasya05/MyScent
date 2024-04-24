package com.hika.user.features.history

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseActivity
import com.hika.user.databinding.ActivityHistoryBinding
import com.hika.user.features.history.payment.HistoryPaymentActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {

    private val viewModel by viewModel<HistoryViewModel>()

    private val historyAdapter by lazy {
        com.hika.user.adapter.HistoryAdapter(
            onPositiveButtonClick = { historyId, _ ->
                val intent = Intent(this, HistoryPaymentActivity::class.java)
                intent.putExtra(HistoryPaymentActivity.HISTORY_ID, historyId)
                startActivity(intent)
            },
            onNegativeButtonClick = { historyId, reason ->
                viewModel.rejectHistory(historyId, reason)
                dismiss()
            }
        )
    }

    override fun inflateViewBinding(): ActivityHistoryBinding {
        return ActivityHistoryBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun onResume() {
        super.onResume()
        viewModel.getHistories()
    }

    override fun ActivityHistoryBinding.bind() {
        rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isError) showErrorSnackBar(it.errorMessage)
                if (it.isSuccess) historyAdapter.submitList(it.successData)
            }
        }
    }
}