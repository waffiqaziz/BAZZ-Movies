# Keep line numbers & source file info for better stack traces (Crashlytics)
-keepattributes LineNumberTable,SourceFile

# Renames all source file attributes to just SourceFile. This prevents leaking
# real filenames in stack traces, but still keeps the attribute for debugging
-renamesourcefileattribute SourceFile

# Preserves generic type information (e.g. List<String>). Needed for libraries
# that use reflection (Gson, Moshi, Retrofit).
-keepattributes Signature

# Keep generics & annotations for libraries using reflection (Hilt, Room, Retrofit, etc.)
-keepattributes *Annotation*

# Suppresses warnings about Java 9+ string concatenation API
-dontwarn java.lang.invoke.StringConcatFactory

# LOG
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

##---------------Begin: proguard configuration for Others  ----------
-keep class io.github.glailton.expandabletextview.ExpandableTextView { <init>(android.content.Context, android.util.AttributeSet); }
##---------------End: proguard configuration for Others  ----------
