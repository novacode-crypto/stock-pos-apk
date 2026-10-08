package com.stockpos.terminal.data.repo

import com.stockpos.terminal.data.dao.SettingsDao
import com.stockpos.terminal.data.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dao: SettingsDao) {
    suspend fun get(key: String): String? = dao.get(key)?.value
    suspend fun set(key: String, value: String) = dao.set(SettingsEntity(key, value))
    suspend fun getServerUrl(): String? = dao.getServerUrl()
    fun getAll(): Flow<List<SettingsEntity>> = dao.getAll()
}
