-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepattributes *Annotation*

# LOG
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

##---------------Begin: proguard configuration for Firebase crashlytics  ----------
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
##---------------End: proguard configuration for Firebase crashlytics  ----------

##---------------Begin: proguard configuration for Others  ----------
-keep class io.github.glailton.expandabletextview.ExpandableTextView { <init>(android.content.Context, android.util.AttributeSet); }
##---------------End: proguard configuration for Others  ----------

# Keep generated Hilt components
-keep class dagger.** { *; }
-dontwarn dagger.**
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class * extends dagger.hilt.android.HiltWrapper_** { *; }
