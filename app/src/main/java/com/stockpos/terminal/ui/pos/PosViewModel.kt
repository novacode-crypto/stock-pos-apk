package com.stockpos.terminal.ui.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stockpos.terminal.StockPOSApp
import com.stockpos.terminal.data.entity.CategoryEntity
import com.stockpos.terminal.data.entity.ProductEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class PosUiState(
    val searchQuery: String = "",
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategory: String? = null,
    val allProducts: List<ProductEntity> = emptyList(),
    val filteredProducts: List<ProductEntity> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val cartTotal: Double = 0.0
)

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
) {
    val total: Double get() = product.price * quantity
}

class PosViewModel : ViewModel() {
    private var app: StockPOSApp? = null

    fun init(app: StockPOSApp) {
        this.app = app
        loadProducts()
    }

    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState.asStateFlow()

    private fun loadProducts() {
        viewModelScope.launch {
            app?.productRepo?.getAllActive()?.collect { products ->
                _uiState.update { it.copy(allProducts = products, filteredProducts = products) }
            }
        }
        viewModelScope.launch {
            app?.database?.categoryDao()?.getAll()?.collect { cats ->
                _uiState.update { it.copy(categories = cats) }
            }
        }
    }

    fun onSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filter()
    }

    fun selectCategory(catId: String?) {
        _uiState.update { it.copy(selectedCategory = catId) }
        filter()
    }

    private fun filter() {
        val state = _uiState.value
        var list = state.allProducts
        if (state.selectedCategory != null) {
            list = list.filter { it.categoryId == state.selectedCategory }
        }
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.sku.lowercase().contains(q) ||
                it.barcode.contains(q)
            }
        }
        _uiState.update { it.copy(filteredProducts = list) }
    }

    fun addToCart(product: ProductEntity) {
        val cart = _uiState.value.cartItems.toMutableList()
        val existing = cart.find { it.product.id == product.id }
        if (existing != null) {
            cart.remove(existing)
            cart.add(existing.copy(quantity = existing.quantity + 1))
        } else {
            cart.add(CartItem(product, 1))
        }
        _uiState.update { it.copy(cartItems = cart, cartTotal = cart.sumOf { it.total }) }
    }

    fun checkout() {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        viewModelScope.launch {
            val app = app ?: return@launch
            val items = state.cartItems.map { cartItem ->
                com.stockpos.terminal.data.entity.SaleItemEntity(
                    id = "item-" + UUID.randomUUID().toString().take(8),
                    saleId = "", // will be set by repo
                    productId = cartItem.product.id,
                    name = cartItem.product.name,
                    sku = cartItem.product.sku,
                    price = cartItem.product.price,
                    cost = cartItem.product.cost,
                    quantity = cartItem.quantity,
                    taxRate = cartItem.product.taxRate,
                    total = cartItem.total
                )
            }

            val subtotal = state.cartTotal
            val profit = state.cartItems.sumOf { (it.product.price - it.product.cost) * it.quantity }

            app.saleRepo.createSale(
                items = items,
                customerName = "Sin registrar",
                paymentMethod = "efectivo",
                discountPct = 0.0,
                subtotal = subtotal,
                discount = 0.0,
                tax = 0.0,
                total = subtotal,
                profit = profit
            )

            // Update stock
            state.cartItems.forEach { cartItem ->
                val newStock = cartItem.product.stock - cartItem.quantity
                app.productRepo.updateStock(cartItem.product.id, newStock)
            }

            // Clear cart
            _uiState.update { it.copy(cartItems = emptyList(), cartTotal = 0.0) }

            // Try sync
            val serverUrl = app.settingsRepo.getServerUrl()
            if (serverUrl != null) {
                app.saleRepo.syncToServer(serverUrl)
            }
        }
    }
}
