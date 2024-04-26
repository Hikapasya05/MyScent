package com.hika.auth.features.auth.login

import com.hika.common.base.BaseState

data class LoginState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit = Unit
): BaseState<Unit>()
