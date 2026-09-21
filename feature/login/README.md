# :feature:login Module

[![Code Coverage][feature-login-coverage-badge]][feature-login-coverage-link]

## Dependency Graph

```mermaid
%%{
  init: {
    'theme': 'neo-dark'
  }
}%%

graph LR
  subgraph :core
    :core:utils["utils"]
    :core:common["common"]
    :core:designsystem["designsystem"]
    :core:model["model"]
    :core:test["test"]
    :core:instrumentationtest["instrumentationtest"]
    :core:testmodule["testmodule"]
    :core:uihelper["uihelper"]
    :core:user["user"]
  end
  :feature:login --> :core:instrumentationtest
  :feature:login --> :core:testmodule
  :feature:login --> :core:designsystem
  :feature:login --> :navigation
  :feature:login --> :core:common
  :feature:login --> :core:model
  :feature:login --> :core:uihelper
  :feature:login --> :core:utils
  :feature:login --> :core:user
  :feature:login --> :core:test
```

## Overview

`:feature:login` module is responsible for handling user authentication within the application.

## Structure

### Dependency Injection

- **[AuthTMDbAccountUseCaseModule](../login/src/main/kotlin/com/waffiq/bazz_movies/feature/login/di/AuthTMDbAccountUseCaseModule.kt)**:
  Provides dependencies related to TMDb authentication use cases.

### UI Layer

- **[LoginActivity](../login/src/main/kotlin/com/waffiq/bazz_movies/feature/login/ui/LoginActivity.kt)**:
  The main login screen where users enter credentials.
- **[AuthenticationViewModel](../login/src/main/kotlin/com/waffiq/bazz_movies/feature/login/ui/AuthenticationViewModel.kt)**:
  Manages authentication logic and communicates with the domain layer.

### Utils

- **[CustomTypefaceSpan](../login/src/main/kotlin/com/waffiq/bazz_movies/feature/login/utils/CustomTypefaceSpan.kt)**: Utility class for customizing text appearance.
- **[Constants](../login/src/main/kotlin/com/waffiq/bazz_movies/feature/login/utils/common/Constants.kt)**: Common constants used in the login feature.

## Navigation

To navigate to the login screen from another part of the app:

```kotlin
val intent = Intent(context, LoginActivity::class.java)
context.startActivity(intent)
```

<!-- LINK -->

[feature-login-coverage-badge]: https://codecov.io/gh/waffiqaziz/BAZZ-Movies/branch/main/graph/badge.svg?flag=feature-login
[feature-login-coverage-link]: https://app.codecov.io/gh/waffiqaziz/BAZZ-Movies/tree/main/feature/login/src/main/kotlin/com/waffiq/bazz_movies/feature/login
