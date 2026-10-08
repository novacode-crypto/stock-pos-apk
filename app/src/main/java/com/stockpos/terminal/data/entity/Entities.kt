package com.stockpos.terminal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sku: String,
    val barcode: String,
    val categoryId: String?,
    val cost: Double,
    val price: Double,
    val oldPrice: Double?,
    val taxRate: Double,
    val unit: String,
    val stock: Int,
    val minStock: Int,
    val status: String,
    val image: String?,
    val updatedAt: String
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String?,
    val image: String?,
    val sortOrder: Int
)

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey val id: String,
    val number: Int,
    val date: String,
    val customerId: String?,
    val customerName: String,
    val cashierName: String,
    val subtotal: Double,
    val discount: Double,
    val discountPct: Double,
    val tax: Double,
    val total: Double,
    val profit: Double,
    val paymentMethod: String,
    val status: String,
    val synced: Boolean = false
)

@Entity(tableName = "sale_items")
data class SaleItemEntity(
    @PrimaryKey val id: String,
    val saleId: String,
    val productId: String,
    val name: String,
    val sku: String,
    val price: Double,
    val cost: Double,
    val quantity: Int,
    val taxRate: Double,
    val total: Double
)

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val balance: Double,
    val creditLimit: Double
)

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey val id: String,
    val operationType: String,
    val payload: String,
    val status: String, // pending, syncing, synced, failed
    val createdAt: String,
    val attempts: Int = 0
)
