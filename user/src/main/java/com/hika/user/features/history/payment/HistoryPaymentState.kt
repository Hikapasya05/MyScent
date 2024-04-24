package com.hika.user.features.history.payment

import com.hika.common.base.BaseState
import com.hika.data.model.History

data class HistoryPaymentState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: History? = null
): BaseState<History>()
