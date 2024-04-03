package com.hika.myscent.features.auth.register

import com.hika.common.base.BaseState

data class RegisterState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit = Unit
): BaseState<Unit>()
