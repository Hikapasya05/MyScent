package com.hika.data.data.repository.history

import com.hika.data.model.History
import com.hika.data.model.HistoryBody

interface HistoryRepository {
    suspend fun postHistory(body: HistoryBody): Result<Unit>
    suspend fun getHistories(): Result<List<History>>
    suspend fun getHistoryById(historyId: String): Result<History>
    suspend fun updateHistoryStatus(historyId: String, updatedOrderStatus: String, reason: String?): Result<Unit>
}