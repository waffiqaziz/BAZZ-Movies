-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn java.lang.invoke.StringConcatFactory

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

## Keep generated Hilt components
#-keep class dagger.** { *; }
#-dontwarn dagger.**
#-keep class dagger.hilt.** { *; }
#-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
#-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
#-keep class * extends dagger.hilt.android.HiltWrapper_** { *; }
#
## Dagger/Hilt rules
#-keep class dagger.** { *; }
#-keep class javax.inject.** { *; }
#-keep class com.google.dagger.** { *; }
#-keep @dagger.** class * { *; }
#-keep @javax.inject.** class * { *; }
#-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
#-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
#-keep class dagger.hilt.internal.aggregatedroot.codegen.** { *; }
#-keep class dagger.hilt.internal.processedroot.codegen.** { *; }
#-keepnames class dagger.hilt.**
#-dontwarn dagger.hilt.*