package com.hika.myscent.features.home

import com.hika.myscent.base.BaseState
import com.hika.myscent.model.HomePerfume
import com.hika.myscent.model.Perfume

data class HomeState(
    override val isLoading: Boolean =  false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: Unit = Unit,

    val listType: ListType = ListType.HOME,
    val homePerfumes: List<HomePerfume> = emptyList(),
    val searchedPerfumes: List<Perfume> = emptyList()
): BaseState<Unit>()

enum class ListType {
    HOME, SEARCH
}
