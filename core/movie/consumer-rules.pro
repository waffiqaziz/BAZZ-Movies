# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-dontwarn dagger.hilt.**

# Preserve generated Dagger components and factories
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class * extends dagger.hilt.android.HiltWrapper_** { *; }
-keep class * extends dagger.hilt.android.components.** { *; }


# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


# Keep all classes and members in the specified packages
-keep class com.waffiq.bazz_movies.core.movie.domain.model.** { *; }