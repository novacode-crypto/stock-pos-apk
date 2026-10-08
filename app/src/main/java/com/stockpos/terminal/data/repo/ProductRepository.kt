package com.stockpos.terminal.data.repo

import com.stockpos.terminal.data.dao.ProductDao
import com.stockpos.terminal.data.entity.ProductEntity
import com.stockpos.terminal.network.SyncClient
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val dao: ProductDao,
    private val syncClient: SyncClient
) {
    fun getAllActive(): Flow<List<ProductEntity>> = dao.getAllActive()

    fun search(query: String): Flow<List<ProductEntity>> = dao.search(query)

    suspend fun getById(id: String) = dao.getById(id)

    suspend fun updateStock(id: String, newStock: Int) = dao.updateStock(id, newStock)

    suspend fun syncFromServer(serverUrl: String) {
        try {
            val products = syncClient.fetchProducts(serverUrl)
            dao.upsertAll(products)
        } catch (e: Exception) {
            // Offline — keep local data
        }
    }
}
