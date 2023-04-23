# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class kekmech.ru.**.dto.** { *; }
-keep class retrofit2.** { *; }
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

# TODO: remove when okhttp 5.0 will be released
# https://github.com/tonykolomeytsev/mpeiapp/issues/171
# OkHttp
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Generate mapping files
-dump release/obfuscation/class_files.txt
-printseeds release/obfuscation/seeds.txt
-printusage release/obfuscation/unused.txt
-printmapping release/obfuscation/mapping.txt