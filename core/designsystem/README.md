# \:core\:designsystem Module

>_No Coverage — UI Resources Only_

![Dependency graph](../../docs/images/module-graphs/core-designsystem.svg)

## Overview

`:core:designsystem` is a module dedicated to managing the UI components and design resources used across the application. It provides a centralized place for reusable UI elements, styles, and animations, ensuring consistency and reducing redundancy in the project.

## Responsibilities

- **UI Resources**
  - **Animations:** Contains reusable animations for UI transitions.
  - **Colors:** Defines the primary, secondary, and state-dependent colors for UI components.
  - **Drawables:** Includes background shapes, ripple effects, icons, and placeholders.
  - **Fonts:** Provides custom fonts for text styling.
  - **Layouts:** Contains XML files for various UI components like toolbar items, lists, and error illustrations.
  - **Themes:** Centralizes app themes and night mode configurations.

## Integration

To use the module, add it as a dependency in `build.gradle` file:

```gradle
dependencies {
    implementation(project(":core:designsystem"))
}
```

## Example Usage

Applying a custom background drawable:

```xml
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_button_group" />
```

## Best Practices

- **Encapsulate reusable UI components** – Avoid duplicating UI resource across modules.
- **Use theme attributes instead of hardcoded values** – Improves flexibility and adaptability to dark mode.

This module provides a structured approach to UI design, making the application more scalable and maintainable.
