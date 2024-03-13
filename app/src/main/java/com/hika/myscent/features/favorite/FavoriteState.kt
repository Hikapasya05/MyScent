package com.hika.myscent.features.favorite

import com.hika.myscent.base.BaseState
import com.hika.myscent.model.Perfume

data class FavoriteState(
    override val isLoading: Boolean = false ,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: List<Perfume> = emptyList()
): BaseState<List<Perfume>>()