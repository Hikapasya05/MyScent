package com.hika.admin.features.product

import com.hika.common.base.BaseState

data class ProductManagerState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit? = null
): BaseState<Unit>()
