package io.github.nullptrx.pangleflutter.v2;

import android.view.Gravity;
import android.widget.FrameLayout;

import com.bytedance.msdk.adapter.util.UIUtils;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.TTVideoOption;

import java.util.HashMap;
import java.util.Map;

import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

public class TTAdSlotManager {


    public static AdSlot getSplashAdSlot(int width, int height ,boolean supportDeepLink){
        AdSlot build = new AdSlot.Builder()
                .setImageAdSize(width, height) // 既适用于原生类型，也适用于模版类型。
                .setSupportDeepLink(supportDeepLink) //NEW ADD
                .setSplashButtonType(TTAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN)
                .setDownloadType(TTAdConstant.DOWNLOAD_TYPE_POPUP)
                .build();
        return build;
    }

    public static AdSlot getInterstitialAdAdSlot(){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();

        AdSlot build = new AdSlot.Builder()
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 注意：插屏暂时支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
                .setTTVideoOption(videoOption)
                .setImageAdSize(600, 600) //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
                .build();
        return build;
    }


    public static AdSlot getRewardAdSlot(int orientation){
        
        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();
        
        Map<String, String> customData = new HashMap<>();
        customData.put(AdSlot.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
        customData.put(AdSlot.CUSTOM_DATA_KEY_GDT, "gdt custom data");
        customData.put(AdSlot.CUSTOM_DATA_KEY_KS, "ks custom data");
        AdSlot build =  new AdSlot.Builder()
                .setTTVideoOption(videoOption)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 确保该聚合广告位下所有gdt代码位都是同一种类型（模版或非模版）。
                .setCustomData(customData) // 激励视频开启服务端验证时，透传给adn的自定义数据。
                .setOrientation(orientation).build();
        return build;
    }


    public static AdSlot getFeedListAdSlot(int orientation){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();
        FrameLayout.LayoutParams gdtNativeAdLogoParams =
                new FrameLayout.LayoutParams(
                        40,
                        13,
                        Gravity.RIGHT | Gravity.TOP); // 例如，放在右上角
        Map<String, String> customData = new HashMap<>();
        customData.put(AdSlot.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
        customData.put(AdSlot.CUSTOM_DATA_KEY_GDT, "gdt custom data");
        customData.put(AdSlot.CUSTOM_DATA_KEY_KS, "ks custom data");
        AdSlot build =  new AdSlot.Builder()
                .setTTVideoOption(videoOption)//视频声音相关的配置
                .setAdStyleType(1)//必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告
                // 备注
                // 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
                // 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
                .setImageAdSize(1080, 340)// 必选参数 单位dp ，详情见上面备注解释
                .setAdCount(3) //请求广告数量为1到3条
                .setGdtNativeAdLogoParams(gdtNativeAdLogoParams).build();
        return build;
    }


    public static AdSlot getFullVideoAdSlot(int orientation){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();


        AdSlot adSlotBuilder = new AdSlot.Builder()
                .setTTVideoOption(videoOption)//设置声音控制
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation).build();
        return adSlotBuilder;
    }
}
