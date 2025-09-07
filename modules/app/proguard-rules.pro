# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Gson
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LinenumberTable
-keep public class * extends java.lang.Exception

# Java 8 API
-keep class java.time.** { *; }

# Generate mapping files
-dump release/obfuscation/class_files.txt
-printseeds release/obfuscation/seeds.txt
-printusage release/obfuscation/unused.txt
-printmapping release/obfuscation/mapping.txt
