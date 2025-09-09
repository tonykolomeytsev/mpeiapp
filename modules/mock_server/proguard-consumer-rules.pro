# Keep Netty tcnative classes
-keep class io.netty.internal.tcnative.** { *; }
-keep interface io.netty.internal.tcnative.** { *; }

# If you are using BoringSSL (common with Netty's native SSL)
-keep class org.wildfly.openssl.** { *; }
-keep interface org.wildfly.openssl.** { *; }

# General Netty rules that might also be helpful
-dontwarn io.netty.**
-keep class io.netty.** { *; }
-keep interface io.netty.** { *; }

# Ktor
-dontwarn java.lang.management.**
-keep class java.lang.management.ManagementFactory { *; }
-keep class java.lang.management.RuntimeMXBean { *; }
-keep class io.ktor.util.debug.IntellijIdeaDebugDetector { *; }

# SLF4J (if you're using it directly or it's a transitive dependency)
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**
