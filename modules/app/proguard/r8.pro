# Generate mapping files
# -printseeds release/obfuscation/seeds.txt
# -printusage release/obfuscation/unused.txt
# -printmapping release/obfuscation/mapping.txt
# -printconfiguration "release/obfuscation/configuration.pro"

-dontwarn com.google.re2j.Matcher
-dontwarn com.google.re2j.Pattern

# Retrofit services are created by the annotation @EndpointUrl that is read at runtime: R8 must not remove it,
# otherwise "interface ... should have @EndpointUrl annotation" breaks every screen that depends on a service
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault,Signature,InnerClasses,EnclosingMethod
-keep @interface kekmech.ru.lib_network.EndpointUrl { *; }
-keep @kekmech.ru.lib_network.EndpointUrl interface * { *; }
