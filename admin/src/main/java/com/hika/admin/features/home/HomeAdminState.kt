package com.hika.admin.features.home

import com.hika.common.base.BaseState
import com.hika.data.model.Perfume

data class HomeAdminState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: List<Perfume>? = emptyList(),

    val listType: ListType = ListType.HOME
): BaseState<List<Perfume>>()

enum class ListType {
    HOME,
    SEARCH
}
