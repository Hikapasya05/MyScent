package com.hika.user.features.payment

import com.hika.common.base.BaseState

data class PaymentState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit? = null
): BaseState<Unit>() {
    fun clear() = PaymentState()
}
