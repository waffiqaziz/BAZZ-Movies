# :core:coroutines Module  

[![Code Coverage][core-coroutines-coverage-badge]][core-coroutines-coverage-link]

## Dependency Graph  

![Dependency graph](../../docs/images/module-graphs/core-coroutines.svg)  

## Overview  

`:core:coroutines` is a module that provides standardized coroutine dispatchers for managing concurrency across the application. It ensures structured and efficient coroutine execution while promoting testability and maintainability.  

## Responsibilities  

- **[Dispatcher Module](../coroutines/src/main/kotlin/com/waffiq/bazz_movies/core/coroutines/DispatcherModule.kt)**  
  - Defines and provides coroutine dispatchers using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?).
  - Ensures a consistent way to inject different dispatchers (`Default`, `IO`, `Main`) throughout the application.  

### Provided Dispatchers  

- `@DefaultDispatcher` – As `Dispatchers.Default`, optimized for CPU-intensive tasks.  
- `@IoDispatcher` – As `Dispatchers.IO`, used for network and disk operations.  
- `@MainDispatcher` – As `Dispatchers.Main`, intended for UI-related tasks.  

## Integration  

To use the module, add it as a dependency in `build.gradle` file:  

```gradle
dependencies {
    implementation(project(":core:coroutines"))
}
```  

## Example Usage  

Inject the desired dispatcher in a class using Hilt:  

```kotlin
@Singleton
class LocalDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocalDataSourceInterface {
    
    suspend fun getFavorites(): List<FavoriteEntity> = withContext(ioDispatcher) {
        favoriteDao.getAllFavorites()
    }
}
```

## Best Practices  

- **Use the correct dispatcher for tasks** – `IO` for disk/network, `Default` for CPU work, and `Main` for UI.  
- **Inject dispatchers instead of using `Dispatchers` directly** – Improves testability by allowing coroutine dispatcher replacements in tests.  
- **Leverage structured concurrency** – Use `withContext(dispatcher)` for better coroutine scoping.  

This module provides a clean and scalable approach to managing coroutine dispatching, ensuring efficient background processing and improved testability.

<!-- Link -->

[core-coroutines-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-coroutines
[core-coroutines-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/coroutines/src/main/kotlin/com/waffiq/bazz_movies/core/coroutines
