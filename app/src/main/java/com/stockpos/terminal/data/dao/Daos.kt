package com.stockpos.terminal.data.dao

import androidx.room.*
import com.stockpos.terminal.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE status IN ('activo','agotado') ORDER BY name")
    fun getAllActive(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE (name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%') AND status IN ('activo','agotado')")
    fun search(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: String): ProductEntity?

    @Query("UPDATE products SET stock = :newStock, status = CASE WHEN :newStock <= 0 THEN 'agotado' ELSE status END WHERE id = :id")
    suspend fun updateStock(id: String, newStock: Int)

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Upsert
    suspend fun upsert(product: ProductEntity)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder")
    fun getAll(): Flow<List<CategoryEntity>>

    @Upsert
    suspend fun upsertAll(categories: List<CategoryEntity>)
}

@Dao
interface SaleDao {
    @Insert
    suspend fun insert(sale: SaleEntity)

    @Query("SELECT * FROM sales ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE synced = 0")
    suspend fun getUnsynced(): List<SaleEntity>

    @Query("UPDATE sales SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("SELECT COUNT(*) FROM sales")
    suspend fun count(): Int

    @Query("SELECT COALESCE(SUM(total), 0) FROM sales WHERE date >= :todayStart")
    fun getTodayTotal(todayStart: String): Flow<Double>
}

@Dao
interface SaleItemDao {
    @Insert
    suspend fun insertAll(items: List<SaleItemEntity>)

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getBySaleId(saleId: String): List<SaleItemEntity>
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE `key` = :key")
    suspend fun get(key: String): SettingsEntity?

    @Insert
    suspend fun set(setting: SettingsEntity)

    @Query("SELECT * FROM settings")
    fun getAll(): Flow<List<SettingsEntity>>

    @Query("SELECT value FROM settings WHERE `key` = 'server_url'")
    suspend fun getServerUrl(): String?
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name")
    fun getAll(): Flow<List<CustomerEntity>>

    @Upsert
    suspend fun upsert(customer: CustomerEntity)

    @Upsert
    suspend fun upsertAll(customers: List<CustomerEntity>)
}

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue WHERE status = 'pending' ORDER BY createdAt")
    suspend fun getPending(): List<SyncQueueEntity>

    @Insert
    suspend fun insert(item: SyncQueueEntity)

    @Query("UPDATE sync_queue SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'pending'")
    fun pendingCount(): Flow<Int>
}
