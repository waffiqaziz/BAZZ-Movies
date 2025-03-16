# :core:mappers Module

## Dependency Graph  

![Dependency graph](../../docs/images/module-graphs/core-mappers.svg)  

## Overview

The `:core:mappers` module is responsible for converting data models between different layers of the application. It helps maintain separation of concerns by ensuring that domain models, data models, and UI models remain distinct and properly formatted.

## Responsibilities

- **[PostMapper](../mappers/src/main/kotlin/com/waffiq/bazz_movies/core/mappers/PostMapper.kt)**
  - Converts `Post` data objects into their domain representations.
  - Ensures proper transformation of API responses before usage.

- **[ResultItemMapper](../mappers/src/main/kotlin/com/waffiq/bazz_movies/core/mappers/ResultItemMapper.kt)**
  - Maps `ResultItem` objects between data and domain layers.
  - Handles cases where fields require formatting or type conversion.

- **[StateMapper](../mappers/src/main/kotlin/com/waffiq/bazz_movies/core/mappers/StateMapper.kt)**
  - Maps different states (e.g., loading, success, error) between repository and UI layers.
  - Standardizes state management across the app.

## Integration

To use the module, add it as a dependency in `build.gradle` file:

```gradle
dependencies {
    implementation(project(":core:mappers"))
}
```

## Example Usage

```kotlin
val postDomainModel = apiPostResponse.toPost()
```

## Best Practices

- **Keep mappers stateless** – They should be pure functions with no side effects.
- **Ensure proper error handling** – Handle nullability and type mismatches appropriately.
- **Follow a consistent mapping strategy** – Maintain clear naming conventions to improve readability.

This module plays a crucial role in ensuring data consistency and maintainability across the application.
