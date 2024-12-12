# Convention Plugins

The `build-logic` folder defines project-specific convention plugins, used to keep a single
source of truth for common module configurations.

This approach is heavily based on

- [https://developer.squareup.com/blog/herding-elephants/](https://developer.squareup.com/blog/herding-elephants/)
- [https://github.com/jjohannes/idiomatic-gradle](https://github.com/jjohannes/idiomatic-gradle).
- [https://medium.com/@yudistirosaputro/gradle-convention-plugins-a-powerful-tool-for-reusing-build-configuration-ba2b250d9063](https://medium.com/@yudistirosaputro/gradle-convention-plugins-a-powerful-tool-for-reusing-build-configuration-ba2b250d9063)

By setting up convention plugins in `build-logic`, we can avoid duplicated build script setup,
messy `subproject` configurations, without the pitfalls of the `buildSrc` directory.

`build-logic` is an included build, as configured in the root
[`settings.gradle.kts`](../settings.gradle.kts).

Inside `build-logic` is a `convention` module, which defines a set of plugins that all normal
modules can use to configure themselves.

`build-logic` also includes a set of `Kotlin` files used to share logic between plugins themselves,
which is most useful for configuring Android components (libraries vs applications) with shared
code.

These plugins are *additive* and try to accomplish a single responsibility.
Modules can then pick and choose the configurations they need via `libs.version.toml`.
If there is one-off logic for a module without shared code, it's preferable to define that directly
in the module's `build.gradle`, as opposed to creating a convention plugin with module-specific
setup.

Current list of convention plugins:

- [`bazzmovies.android.application`](convention/src/main/kotlin/AndroidApplicationConventionPlugin.kt)
- [`bazzmovies.android.feature`](convention/src/main/kotlin/AndroidFeatureConventionPlugin.kt)
- [`bazzmovies.android.library`](convention/src/main/kotlin/AndroidLibraryConventionPlugin.kt)
- [`bazzmovies.android.application.firebase`](convention/src/main/kotlin/AndroidApplicationFirebaseConventionPlugin.kt)
- [`bazzmovies.android.lint`](convention/src/main/kotlin/AndroidLintConventionPlugin.kt)
- [`bazzmovies.android.room`](convention/src/main/kotlin/AndroidRoomConventionPlugin.kt)
- [`bazzmovies.hilt`](convention/src/main/kotlin/HiltConventionPlugin.kt)
- [`bazzmovies.hilt.test`](convention/src/main/kotlin/HiltTestConventionPlugin.kt)
- [`bazzmovies.glide`](convention/src/main/kotlin/GlideConventionPlugin.kt)
- [`bazzmovies.jvm.library`](convention/src/main/kotlin/JvmLibraryConventionPlugin.kt)
