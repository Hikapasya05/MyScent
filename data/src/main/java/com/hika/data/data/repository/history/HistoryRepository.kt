package com.hika.data.data.repository.history

import com.hika.data.model.HistoryBody

interface HistoryRepository {
    suspend fun postHistory(body: HistoryBody): Result<Unit>
}