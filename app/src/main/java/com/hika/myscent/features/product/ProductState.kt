package com.hika.myscent.features.product

import com.hika.myscent.base.BaseState
import com.hika.myscent.model.Perfume

data class ProductState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Perfume? = null
): BaseState<Perfume>()