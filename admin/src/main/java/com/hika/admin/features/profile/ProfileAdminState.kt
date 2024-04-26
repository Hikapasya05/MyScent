package com.hika.admin.features.profile

import com.hika.common.base.BaseState
import com.hika.data.model.User

data class ProfileAdminState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: User? = null
): BaseState<User>()
