package com.stockpos.terminal.data.repo

import com.stockpos.terminal.data.dao.SaleDao
import com.stockpos.terminal.data.dao.SaleItemDao
import com.stockpos.terminal.data.dao.SyncQueueDao
import com.stockpos.terminal.data.entity.*
import com.stockpos.terminal.network.SyncClient
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class SaleRepository(
    private val saleDao: SaleDao,
    private val saleItemDao: SaleItemDao,
    private val syncClient: SyncClient
) {
    fun getRecent(limit: Int = 20): Flow<List<SaleEntity>> = saleDao.getRecent(limit)

    fun getTodayTotal(todayStart: String): Flow<Double> = saleDao.getTodayTotal(todayStart)

    suspend fun count() = saleDao.count()

    suspend fun createSale(
        items: List<SaleItemEntity>,
        customerName: String,
        paymentMethod: String,
        discountPct: Double,
        subtotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        profit: Double
    ): SaleEntity {
        val saleId = "sale-" + UUID.randomUUID().toString().take(12)
        val number = (saleDao.count() + 1)
        val now = java.time.Instant.now().toString()

        val sale = SaleEntity(
            id = saleId,
            number = number,
            date = now,
            customerId = null,
            customerName = customerName,
            cashierName = "Vendedor",
            subtotal = subtotal,
            discount = discount,
            discountPct = discountPct,
            tax = tax,
            total = total,
            profit = profit,
            paymentMethod = paymentMethod,
            status = "completada",
            synced = false
        )

        saleDao.insert(sale)
        saleItemDao.insertAll(items)

        // Enqueue for sync
        val payload = "${saleId}|||${number}|||${total}|||${paymentMethod}"
        // syncQueueDao would be injected in full version

        return sale
    }

    suspend fun getUnsynced() = saleDao.getUnsynced()

    suspend fun markSynced(id: String) = saleDao.markSynced(id)

    suspend fun syncToServer(serverUrl: String) {
        val unsynced = saleDao.getUnsynced()
        for (sale in unsynced) {
            try {
                val items = saleItemDao.getBySaleId(sale.id)
                syncClient.sendSale(serverUrl, sale, items)
                saleDao.markSynced(sale.id)
            } catch (e: Exception) {
                // Keep in queue for retry
            }
        }
    }
}
