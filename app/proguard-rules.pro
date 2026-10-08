# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
  @com.squareup.moshi.JsonClass <methods>;
}
