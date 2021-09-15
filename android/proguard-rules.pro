
-optimizationpasses 5

#混淆时不会产生形形色色的类名
-dontusemixedcaseclassnames

#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#不预校验
-dontpreverify

#不优化输入的类文件
-dontoptimize

-ignorewarnings

-verbose

#优化
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护内部类
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# pangle穿山甲 原有的
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.** {*;}
-keep class com.bytedance.embed_dr.** {*;}
-keep class com.bytedance.embedapplog.** {*;}

##插件新增  穿山甲插件化版本新增
-keep public class com.ss.android.**{*;}
-keeppackagenames com.bytedance.sdk.openadsdk.api
-keeppackagenames com.bytedance.embed_dr
-keeppackagenames com.bytedance.embedapplog
-keeppackagenames com.ss.android

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
