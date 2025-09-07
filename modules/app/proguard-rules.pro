# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   - http://developer.android.com/guide/developing/tools/proguard.html
#   - https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md

# Generate mapping files
-dump release/obfuscation/class_files.txt
-printseeds release/obfuscation/seeds.txt
-printusage release/obfuscation/unused.txt
-printmapping release/obfuscation/mapping.txt
-printconfiguration "release/obfuscation/configuration.pro"
