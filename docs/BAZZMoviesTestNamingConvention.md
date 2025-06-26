# 🧪 Test Naming Convention

We follow the format:

**Given**__**When**_**Then** structure

This helps clearly express:

- **Given**: What is being tested
- **When**: Under which condition
- **Then**: What the expected outcome is

## Example

```kotlin
loadInitialPage_whenApiCallSucceeds_returnCorrectPage
tiggerButton_withDoubleTouch_shouldDoNothing
fetchUser_withValidValue_callsTheAPI
logout_whenSessionExpired_clearUserSession
dataResponse_withCorrectValues_setsPropertiesCorrectly
```

### Avoid vague names

```kotlin
test1()
checkSomething()
shouldWork()
```

Stick to descriptive, consistent naming across all tests (unit and instrumentation).
