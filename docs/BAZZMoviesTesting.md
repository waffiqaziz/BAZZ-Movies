# 🧪 Testing

We use testing to ensure the stability and quality of our code. Here's how you can run different
types of tests in the project:

## Unit Tests

Run all unit tests with the following command:

``` terminal
./gradlew test
```

The result can see through the console. Unit tests are also automatically executed via GitHub
Actions on every push or pull request. You can review the results directly on the GitHub.
interface.

## UI Tests

To run Android-specific UI tests, use:

``` terminal
./gradlew connectedAndroidTest
```

This runs tests on a connected Android device or emulator.

*Note that UI test coverage is still work in progress.*

## Code Coverage Reports with [JaCoCo](https://github.com/jacoco/jacoco)

Generate a combined coverage report using the following command:

``` terminal
./gradlew create<Variant>CombinedCoverageReport
```

### Generating a Report for a specific module

For [`:core:user`](../core/user/) module on debug variant:

- Clean and run unit tests:

  ``` terminal
  ./gradlew clean :core:user:testDebugUnitTest
  ```

- Generate a combined coverage report:

  ``` terminal
  ./gradlew :core:user:createDebugCombinedCoverageReport
  ```

- The generate report available on

  ``` terminal
  core/user/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
  ```

### Generating a Report for all Module

- Clean and run unit tests:

  ``` terminal
  ./gradlew clean testDebugUnitTest
  ```

- Generate a combined coverage report:

  ``` terminal
  ./gradlew createDebugCombinedCoverageReport
  ```

- The generate report available on all module separately.
  Example for [:core:mappers](../core/mappers/) module, the report available on

  ``` terminal
  core/mappers/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
  ```
