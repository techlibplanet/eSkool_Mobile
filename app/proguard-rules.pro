# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Software\Folder\Android\adt-bundle-windows-x86_64-20130219\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
#Error view
-keep public class tr.xip.errorview.**
#Progress loading
-keep public class com.wang.avi.**
#RxPermissions2
-keep public class com.tbruyelle.rxpermissions2.**
-keepclassmembers class com.tbruyelle.rxpermissions2.** {*;}
-keepclassmembers enum * { *; }

#Gson
#-dontwarn sun.misc.**
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer

#models
#-keep public class net.rmitsolutions.eskool.models.**
#-keep public class net.rmitsolutions.eskool.viewmodels.**

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

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepattributes *Annotation*

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
