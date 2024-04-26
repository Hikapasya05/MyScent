package com.hika.user.features.product

import com.hika.common.base.BaseState
import com.hika.data.model.Perfume

data class ProductState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Perfume? = null
): BaseState<Perfume>()