# :feature:detail Module

## Dependency Graph

![Dependency graph](../../docs/images/module-graphs/feature-detail.svg)

## Overview

The `:feature:detail` module handles displaying detailed information about movies and TV shows. It integrates data retrieval, domain logic, and UI components to present comprehensive content.

## Structure

### Data Layer

- **Repository Implementation** – [`DetailRepositoryImpl`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/data/repository/DetailRepositoryImpl.kt) handles data fetching and processing.

### Domain Layer

- **Models** – Defines structured data objects for movies, TV shows, and additional details such as ratings and production companies.
- **Repository Interface** – [`IDetailRepository`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/domain/repository/IDetailRepository.kt) abstracts data operations.
- **Use Cases** – Includes interactor classes for fetching movie, TV, and OMDb details.

### UI Layer

- **Activities & ViewModels** – [`DetailMovieActivity`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/ui/DetailMovieActivity.kt) manages UI, while [`DetailMovieViewModel`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/ui/DetailMovieViewModel.kt) and [`DetailUserPrefViewModel`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/ui/DetailUserPrefViewModel.kt) handle state management.
- **Adapters** – [`CastAdapter`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/ui/adapter/CastAdapter.kt), [`RecommendationAdapter`](../detail/src/main/kotlin/com/waffiq/bazz_movies/feature/detail/ui/adapter/RecommendationAdapter.kt) for rendering lists.

### Utilities

- **Helpers** – Functions for handling ratings, release dates, and UI components.
- **Mappers** – Convert raw data into structured domain models.

## Integration

To use this module, add it as a dependency:

```gradle
dependencies {
    implementation(project(":feature:detail"))
}
```

## Navigation

To open the detail screen:

```kotlin
val intent = Intent(context, DetailMovieActivity::class.java)
context.startActivity(intent)
```
