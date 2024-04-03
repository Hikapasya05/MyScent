package com.hika.myscent.features.favorite

import com.hika.common.base.BaseState
import com.hika.data.model.Perfume

data class FavoriteState(
    override val isLoading: Boolean = false ,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: List<Perfume> = emptyList()
): BaseState<List<Perfume>>()