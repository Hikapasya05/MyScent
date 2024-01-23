package com.hika.myscent.features.home

import com.hika.myscent.base.BaseState
import com.hika.myscent.model.Perfume

data class HomeState(
    override val isLoading: Boolean =  false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Map<String, List<Perfume>>? = mapOf()
): BaseState<Map<String, List<Perfume>>>()
