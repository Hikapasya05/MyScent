package com.hika.user.features.history

import com.hika.common.base.BaseState
import com.hika.data.model.History

data class HistoryState(
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val errorMessage: String = "",
    override val isSuccess: Boolean = false,
    override val successData: List<History> = emptyList()
): BaseState<List<History>>()
