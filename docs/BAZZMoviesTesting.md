# 🧪 Testing

We use testing to ensure the stability and quality of our code. Here's how you can run different
types of tests in the project:

1. **Unit Tests (Status: Still in Progress)**

   Run all unit tests with the following command:

   ``` terminal
   ./gradlew test
   ```

   The result can see through the console. Unit tests are also automatically executed via GitHub
   Actions on every push or pull request. You can review the results directly on the GitHub.
   interface.

2. **UI Tests (Status: Still in Progress)**

   To run Android-specific UI tests, use:

   ``` terminal
   ./gradlew connectedAndroidTest
   ```

   This runs tests on a connected Android device or emulator.

   *Note that UI test coverage is still work in progress.*

3. **Code Coverage Reports with JaCoCo**

   Generate a combined coverage report using the following command:

   ``` terminal
   ./gradlew create<Variant>CombinedCoverageReport
   ```

   Example: Generating a Report for a Specific Module
   For the [`:core:user`](../core/user/) module, follow these steps:

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
     core/network/build/reports/jacoco/createDebugCombinedCoverageReport/html/index.html
     ```
