package com.stockpos.terminal

import android.app.Application
import com.stockpos.terminal.data.db.AppDatabase
import com.stockpos.terminal.data.repo.ProductRepository
import com.stockpos.terminal.data.repo.SaleRepository
import com.stockpos.terminal.data.repo.SettingsRepository
import com.stockpos.terminal.network.SyncClient

class StockPOSApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val syncClient by lazy { SyncClient() }
    val productRepo by lazy { ProductRepository(database.productDao(), syncClient) }
    val saleRepo by lazy { SaleRepository(database.saleDao(), database.saleItemDao(), syncClient) }
    val settingsRepo by lazy { SettingsRepository(database.settingsDao()) }
}
