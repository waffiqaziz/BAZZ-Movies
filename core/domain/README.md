# :core:domain Module

[![Code Coverage][core-domain-coverage-badge]][core-domain-coverage-link]

## Overview

`:core:domain` is responsible as shared core business models and result states used throughout the application. It acts as the intermediary between data sources and the presentation layer, ensuring a clean separation of concerns.

## Responsibilities

- **Domain Models**

  - Represents entities such as `Favorite`, `Post`, `Rated`, `UserModel`, and `WatchlistModel`.
  - Provides a structured way to model application-specific data.

- **State Management**
  - Uses [`Outcome`](../domain/src/main/kotlin/com/waffiq/bazz_movies/core/domain/Outcome.kt) to represent success, loading, and error states.

## Integration

To use the module, add it as a dependency in `build.gradle` file:

```gradle
dependencies {
    implementation(project(":core:domain"))
}
```

## Example Usage

Using `Outcome` for result handling:

```kotlin
fun fetchMovies(): Outcome<List<Movie>> {
    return try {
        val movies = movieRepository.getMovies()
        Outcome.Success(movies)
    } catch (e: Exception) {
        Outcome.Error("Error Message")
    }
}
```

## Best Practices

- Keep this module **free of dependencies** on other layers (e.g., UI or data sources).
- Use sealed classes [`Outcome`](./src/main/kotlin/com/waffiq/bazz_movies/core/domain/Outcome.kt) for **consistent state representation**.
- Ensure domain models **only contain business logic** and remain independent module.

This module plays a crucial role in maintaining a well-structured architecture by defining core entities and managing application states effectively.

<!-- LINK -->

[core-domain-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=core-domain
[core-domain-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/core/domain/src/main/kotlin/com/waffiq/bazz_movies/core/domain
