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


# 保留项目中的公共类和接口
-keep class com.clashsing.proxylib.** { *; }

# 保留自定义序列化器
-keep class com.clashsing.proxylib.schema.** { *; }
-keep class com.clashsing.proxylib.parser.** { *; }
-keep class com.clashsing.proxylib.schema.singbox.** { *; }
-keep class com.clashsing.proxylib.schema.clash.** { *; }

# Kotlinx Serialization rules
# Keep kotlinx.serialization specific classes
-keep class kotlinx.serialization.** { *; }
-keep class kotlin.text.RegexOption # Referenced by @Serializable(with=RegexSerializer::class)

# Keep all classes annotated with @Serializable and their members
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
    @kotlinx.serialization.Serializable <methods>;
}
-keepnames class * {
    @kotlinx.serialization.Serializable <fields>;
}

# Keep the companion objects of serializable classes and their members, especially $serializer field and serializer() method
#-keepclassmembers class * {
#    @kotlinx.serialization.Serializable ** Companion;
#    @kotlinx.serialization.Serializable companion object {
#        kotlinx.serialization.KSerializer $serializer;
#        kotlinx.serialization.KSerializer serializer(...);
#    }
#}

#-keepnames class * {
#    @kotlinx.serialization.Serializable companion <fields>;
#}

# Keep all custom serializers and their members

# Keep classes that are themselves annotated with @kotlinx.serialization.Serializer (e.g. custom serializer objects/classes)
-keep @kotlinx.serialization.Serializer class * { *; }
-keepclassmembers @kotlinx.serialization.Serializer class * { *; }

# Keep classes that implement KSerializer directly, their constructors and methods
-keep public class * implements kotlinx.serialization.KSerializer {
    public <init>(...);
    <methods>;
}

# Keep internal serialization classes if necessary (often handled by the above rules)
-keepclassmembers class kotlinx.serialization.internal.* {
    <fields>;
    <methods>;
}

# For Ktor (if used with kotlinx.serialization)
#-if class io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer { \
#    -keepnames class kotlinx.serialization.SerializersKt \
#    -keepclassmembernames class kotlinx.serialization.SerializersKt { \
#        kotlinx.serialization.KSerializer serializer(...); \
#    } \
#}
#-if class io.ktor.serialization.kotlinx.KotlinxSerializationConverter { \
#    -keepnames class kotlinx.serialization.SerializersKt \
#    -keepclassmembernames class kotlinx.serialization.SerializersKt { \
#        kotlinx.serialization.KSerializer serializer(...); \
#    } \
#}

# 保护 kotlinx.serialization 库的相关类
-keep class kotlinx.serialization.** { *; }
-keep class kotlin.reflect.** { *; }

# 保护使用 @Serializable 注解的类及其伴生对象
-keep @kotlinx.serialization.Serializable class * {
    # 保护所有字段
    *;
}

# 保护伴生对象中的序列化相关方法
-keepclassmembers class * {
    static <fields>;
    static <methods>;
}

# 保护自定义序列化器
-keep class * implements kotlinx.serialization.KSerializer { *; }

# 保护使用 @SerialName 注解的字段
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# Keep intrinsic Kotlin objects often used with serialization
-keepclassmembers class **.$DefaultImpls { *; }

# SnakeYAML rules
# Keep SnakeYAML classes that are used, especially those instantiated via reflection
-keep class org.yaml.snakeyaml.** { *; }
-keepclassmembers class org.yaml.snakeyaml.** { *; }

# If you have specific data classes that SnakeYAML deserializes into,
# and they are NOT @Serializable (which would be covered above),
# you might need to keep them explicitly:
# -keep class com.yourpackage.YourYamlDataClass { *; }
# -keepclassmembers class com.yourpackage.YourYamlDataClass { *; }

-dontwarn java.beans.BeanInfo
-dontwarn java.beans.FeatureDescriptor
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn org.yaml.snakeyaml.**

