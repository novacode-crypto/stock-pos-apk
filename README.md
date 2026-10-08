# Stock & POS — Terminal POS Android

## Terminal POS para vendedores

App Android nativa (Kotlin + Jetpack Compose) que funciona como terminal POS.
Sincroniza con la PWA de escritorio (centro de control) vía WiFi local.

## Requisitos

- Android Studio Hedgehog (2023.1) o superior
- JDK 17
- Android SDK 34 (compileSdk)
- minSdk 26 (Android 8.0+)

## Estructura

```
app/src/main/java/com/stockpos/terminal/
├── StockPOSApp.kt          ← Application class (DI manual)
├── data/
│   ├── db/                 ← Room database + converters
│   ├── dao/                ← DAOs (Product, Sale, Category, etc.)
│   ├── entity/             ← Entities (Room)
│   └── repo/               ← Repositories (Product, Sale, Settings)
├── network/
│   └── SyncClient.kt       ← Retrofit client para sync con PWA
└── ui/
    ├── MainActivity.kt     ← Navigation host
    ├── theme/              ← Material 3 theme (mismos colores que PWA)
    ├── login/              ← Login screen
    └── pos/                ← POS screen + ViewModel
```

## Arquitectura

- **MVVM** con ViewModel + StateFlow
- **Room** para almacenamiento local (offline-first)
- **Retrofit** para sincronización con la PWA
- **Jetpack Compose** para UI (Material 3)
- **Coroutines** para async

## Sincronización

1. La APK se conecta al servidor local de la PWA (`http://[IP-PC]:3000`)
2. **Pull**: descarga catálogo de productos actualizado
3. **Push**: envía ventas realizadas offline
4. Operaciones idempotentes (basadas en IDs únicos)

## Endpoints esperados (PWA server)

```
GET  /api/products     → lista de productos
POST /api/sales/sync   → recibir venta desde la APK
GET  /api/categories   → lista de categorías
```

## Credenciales por defecto

```
Usuario: vendedor
Clave:  (asignada por el admin desde la PWA)
```

## Build

```bash
# Abrir en Android Studio y build, o desde CLI:
./gradlew assembleDebug
```

## Estado

- ✅ Estructura del proyecto
- ✅ Room database schema
- ✅ DAOs para todas las entidades
- ✅ Repositories con sync
- ✅ Login screen (Compose)
- ✅ POS screen (Compose) con grid + carrito + checkout
- ✅ Theme Material 3 con colores de la PWA
- 🚧 Sync real (cuando la PWA exponga los endpoints)
- 🚧 Pantalla de configuración (IP del servidor)
