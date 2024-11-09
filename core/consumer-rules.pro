# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-dontwarn dagger.hilt.**

# Keep all Hilt-related generated factories in the specified package
-keep class com.waffiq.bazz_movies.core.di.** { *; }
-dontwarn com.waffiq.bazz_movies.core.di.**

# Preserve generated Dagger components and factories
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class * extends dagger.hilt.android.HiltWrapper_** { *; }
-keep class * extends dagger.hilt.android.components.** { *; }

# Keep the DatabaseModule and any other Hilt modules explicitly
-keep class com.waffiq.bazz_movies.core.di.DatabaseModule
-keep class com.waffiq.bazz_movies.Hilt_** { *; }


# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


# Keep all classes and members in the specified packages
-keep class com.waffiq.bazz_movies.core.data.local.model.** { *; }
-keep class com.waffiq.bazz_movies.core.data.remote.post_body.** { *; }
-keep class com.waffiq.bazz_movies.core.utils.result.SnackBarUserLoginData { *; }
-keep class com.waffiq.bazz_movies.core.utils.resultstate.** { *; }
-keep class com.waffiq.bazz_movies.core.data.remote.post_body.** { *; }
-keep class com.waffiq.bazz_movies.core.data.local.model.** { *; }
-keep class com.waffiq.bazz_movies.core.domain.model.** { *; }