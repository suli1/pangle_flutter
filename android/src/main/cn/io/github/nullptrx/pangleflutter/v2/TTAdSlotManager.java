package io.github.nullptrx.pangleflutter.v2;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bytedance.msdk.adapter.util.UIUtils;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.TTVideoOption;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.github.nullptrx.pangleflutter.common.PangleOrientation;


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

    public static AdSlot getInterstitialAdAdSlot(Float width,Float height,boolean supportDeepLink){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();

        AdSlot build = new AdSlot.Builder()
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 注意：插屏暂时支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
                .setTTVideoOption(videoOption)
                .setSupportDeepLink(supportDeepLink)
                .setImageAdSize(width == null ? 0:width.intValue(), height == null ? 0 : height.intValue()) //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
                .build();
        return build;
    }


    public static AdSlot getRewardAdSlot(MethodCall call){
        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();
        AdSlot.Builder builder = new AdSlot.Builder().setTTVideoOption(videoOption);

        String slotId = call.argument("slotId");
        String userId = call.argument("userId");
        if(!TextUtils.isEmpty(userId)){
            builder.setUserID(userId);
        }
        String rewardName = call.argument("rewardName");
        if(!TextUtils.isEmpty(rewardName)){
            builder.setRewardName(rewardName);
        }
        Integer rewardAmount = call.argument("rewardAmount");

        if(!TextUtils.isEmpty(rewardName)){
            builder.setRewardAmount(rewardAmount);
        }

        String extra = call.argument("extra");

        Boolean isVertical = call.argument("isVertical");
        builder.setOrientation((isVertical == null || isVertical) ? TTAdConstant.VERTICAL : TTAdConstant.HORIZONTAL);

        Boolean isSupportDeepLink = call.argument("isSupportDeepLink");
        Map<String, Double> expressArgs = call.argument("expressSize");
        if(expressArgs != null){
            Double w = expressArgs.get("width");
            if(w == null){
                w = 0.00;
            }
            Double h = expressArgs.get("height");
            if(h == null){
                h = 0.00;
            }
            builder.setImageAdSize(w.intValue(),h.intValue());
        }

        Map<String, String> customData = new HashMap<>();
        customData.put(AdSlot.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
        customData.put(AdSlot.CUSTOM_DATA_KEY_GDT, "gdt custom data");
        customData.put(AdSlot.CUSTOM_DATA_KEY_KS, "ks custom data");

        AdSlot build = builder
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 确保该聚合广告位下所有gdt代码位都是同一种类型（模版或非模版）。
                .setCustomData(customData) // 激励视频开启服务端验证时，透传给adn的自定义数据。
                .build();
        return build;
    }


    public static AdSlot getFeedListAdSlot(MethodCall call){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();
        AdSlot.Builder builder = new AdSlot.Builder().setTTVideoOption(videoOption);


        Boolean isSupportDeepLink = call.argument("isSupportDeepLink");
        if(isSupportDeepLink == null){
            isSupportDeepLink = false;
        }
        Map<String, Double> expressArgs = call.argument("expressSize");
        if(expressArgs != null){
            Double w = expressArgs.get("width");
            if(w == null){
                w = 0.00;
            }
            Double h = expressArgs.get("height");
            if(h == null){
                h = 0.00;
            }
            builder.setImageAdSize(w.intValue(),h.intValue());// 必选参数 单位dp ，详情见上面备注解释
        }
        Integer count =  call.argument("count");

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
                .setAdStyleType(1)//必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告
                // 备注
                // 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
                // 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
                .setAdCount(count == null ? 0 : count) //请求广告数量为1到3条
                .setGdtNativeAdLogoParams(gdtNativeAdLogoParams).build();
        return build;
    }


    public static AdSlot getFullVideoAdSlot(MethodCall call){

        TTVideoOption videoOption = new TTVideoOption.Builder()
                .setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
                .setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
                .build();
        AdSlot.Builder builder = new AdSlot.Builder().setTTVideoOption(videoOption);
        Integer orientationIndex = call.argument("orientation");
        if(orientationIndex == null){
            builder.setOrientation(TTAdConstant.VERTICAL);
        }
        Boolean isSupportDeepLink = call.argument("isSupportDeepLink");
        if(isSupportDeepLink == null){
            isSupportDeepLink = false;
        }
        Map<String, Double> expressArgs = call.argument("expressSize");
        if(expressArgs != null){
            Double w = expressArgs.get("width");
            if(w == null){
                w = 0.00;
            }
            Double h = expressArgs.get("height");
            if(h == null){
                h = 0.00;
            }
            builder.setImageAdSize(w.intValue(),h.intValue());
        }
        String userId = call.argument("userId"); //必传
        if(!TextUtils.isEmpty(userId)){
            builder.setUserID(userId);
        }
        /*
         AdSlot adSlotBuilder = new AdSlot.Builder()
                .setTTVideoOption(videoOption)//设置声音控制
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation).build();
         */
        AdSlot adSlotBuilder = new AdSlot.Builder()
                .setSupportDeepLink(isSupportDeepLink)
               .build();
        return adSlotBuilder;
    }
}
