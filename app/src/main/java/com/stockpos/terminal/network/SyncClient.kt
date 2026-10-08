package com.stockpos.terminal.network

import com.stockpos.terminal.data.entity.ProductEntity
import com.stockpos.terminal.data.entity.SaleEntity
import com.stockpos.terminal.data.entity.SaleItemEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

data class ProductListResponse(val products: List<ProductDTO>)
data class ProductDTO(
    val id: String, val name: String, val sku: String, val barcode: String,
    val categoryId: String?, val cost: Double, val price: Double,
    val oldPrice: Double?, val taxRate: Double, val unit: String,
    val stock: Int, val minStock: Int, val status: String, val image: String?
)

data class SaleSyncRequest(
    val id: String, val number: Int, val date: String,
    val customerId: String?, val customerName: String,
    val cashierName: String, val subtotal: Double, val discount: Double,
    val discountPct: Double, val tax: Double, val total: Double,
    val profit: Double, val paymentMethod: String, val status: String,
    val items: List<SaleItemDTO>
)

data class SaleItemDTO(
    val productId: String, val name: String, val sku: String,
    val price: Double, val cost: Double, val quantity: Int,
    val taxRate: Double, val total: Double
)

interface SyncApi {
    @GET
    suspend fun fetchProducts(@Url url: String): ProductListResponse

    @POST
    suspend fun sendSale(@Url url: String, @Body sale: SaleSyncRequest): Map<String, String>
}

class SyncClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(SyncApi::class.java)

    suspend fun fetchProducts(serverUrl: String): List<ProductEntity> {
        val response = api.fetchProducts("$serverUrl/api/products")
        return response.products.map { dto ->
            ProductEntity(
                id = dto.id, name = dto.name, sku = dto.sku, barcode = dto.barcode,
                categoryId = dto.categoryId, cost = dto.cost, price = dto.price,
                oldPrice = dto.oldPrice, taxRate = dto.taxRate, unit = dto.unit,
                stock = dto.stock, minStock = dto.minStock, status = dto.status,
                image = dto.image, updatedAt = ""
            )
        }
    }

    suspend fun sendSale(serverUrl: String, sale: SaleEntity, items: List<SaleItemEntity>) {
        val request = SaleSyncRequest(
            id = sale.id, number = sale.number, date = sale.date,
            customerId = sale.customerId, customerName = sale.customerName,
            cashierName = sale.cashierName, subtotal = sale.subtotal,
            discount = sale.discount, discountPct = sale.discountPct,
            tax = sale.tax, total = sale.total, profit = sale.profit,
            paymentMethod = sale.paymentMethod, status = sale.status,
            items = items.map {
                SaleItemDTO(
                    productId = it.productId, name = it.name, sku = it.sku,
                    price = it.price, cost = it.cost, quantity = it.quantity,
                    taxRate = it.taxRate, total = it.total
                )
            }
        )
        api.sendSale("$serverUrl/api/sales/sync", request)
    }
}
