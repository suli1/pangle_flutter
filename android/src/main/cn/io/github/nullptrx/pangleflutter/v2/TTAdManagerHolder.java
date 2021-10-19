package io.github.nullptrx.pangleflutter.v2;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTAdConfig;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTPrivacyConfig;
import com.bytedance.msdk.api.fullVideo.TTFullVideoAd;
import com.bytedance.msdk.api.fullVideo.TTFullVideoAdListener;
import com.bytedance.msdk.api.fullVideo.TTFullVideoAdLoadCallback;
import com.bytedance.msdk.api.interstitial.TTInterstitialAd;
import com.bytedance.msdk.api.interstitial.TTInterstitialAdListener;
import com.bytedance.msdk.api.interstitial.TTInterstitialAdLoadCallback;
import com.bytedance.msdk.api.nativeAd.TTNativeAd;
import com.bytedance.msdk.api.nativeAd.TTNativeAdLoadCallback;
import com.bytedance.msdk.api.nativeAd.TTUnifiedNativeAd;
import com.bytedance.msdk.api.reward.RewardItem;
import com.bytedance.msdk.api.reward.TTRewardAd;
import com.bytedance.msdk.api.reward.TTRewardedAdListener;
import io.flutter.plugin.common.MethodChannel;
import io.github.nullptrx.pangleflutter.PangleAdManager;
import io.github.nullptrx.pangleflutter.bean.PangleResult;
import io.github.nullptrx.pangleflutter.common.PangleLoadingType;
import io.github.nullptrx.pangleflutter.util.MessageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

  private static final String TAG = "PagngleV2";
  private static boolean sInit;

  public static TTPrivacyConfig privacyConfig = new TTPrivacyConfig() {
    @Override
    public boolean isLimitPersonalAds() {
      return true;
    }

    @Override
    public boolean isCanUsePhoneState() {
      return false;
    }

    @Override
    public boolean isCanUseLocation() {
      return false;
    }
  };

  /**
   * @param context 上下文
   * @param map 桥接参数
   */
  public static void init(Context context, Map<String, Object> map, MethodChannel.Result result) {
    doInit(context, map, result);
  }

  //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
  private static void doInit(Context context, Map<String, Object> map,
      MethodChannel.Result result) {
    if (!sInit) {
      sInit = true;
      TTAdConfig ttAdConfig = buildConfig(context, map);
      if (ttAdConfig == null) {
        MessageUtils.postSimpleMessage(result, -1, "init error");
        return;
      }
      TTMediationAdSdk.initialize(context, buildConfig(context, map));
    }
    MessageUtils.postSimpleMessage(result, 0, "init success");
  }

  private static TTAdConfig buildConfig(Context context, Map<String, Object> map) {
    String appId = "";
    if (map.get("appId") != null) {
      appId = (String) map.get("appId");
    }

    boolean debug = false;
    if (map.get("debug") != null) {
      debug = (boolean) map.get("debug");
    }

    boolean allowShowNotify = false;
    if (map.get("allowShowNotify") != null) {
      allowShowNotify = (boolean) map.get("allowShowNotify");
    }

    boolean allowShowPageWhenScreenLock = false;
    if (map.get("allowShowPageWhenScreenLock") != null) {
      allowShowPageWhenScreenLock = (boolean) map.get("allowShowPageWhenScreenLock");
    }

    boolean supportMultiProcess = false;
    if (map.get("supportMultiProcess") != null) {
      supportMultiProcess = (boolean) map.get("supportMultiProcess");
    }

    List<Integer> directDownloadNetworkType = new ArrayList<>();
    if (map.get("directDownloadNetworkType") != null) {
      directDownloadNetworkType = (List<Integer>) map.get("directDownloadNetworkType");
    }

    boolean paid = false;
    if (map.get("paid") != null) {
      paid = (boolean) map.get("paid");
    }

    Integer titleBarThemeIndex = null;
    if (map.get("titleBarThemeIndex") != null) {
      titleBarThemeIndex = (Integer) map.get("titleBarThemeIndex");
    }

    boolean isCanUseLocation = false;
    if (map.get("isCanUseLocation") != null) {
      isCanUseLocation = (boolean) map.get("isCanUseLocation");
    }

    boolean isCanUsePhoneState = false;
    if (map.get("isCanUsePhoneState") != null) {
      isCanUsePhoneState = (boolean) map.get("isCanUsePhoneState");
    }

    boolean isCanUseWriteExternal = false;
    if (map.get("isCanUseWriteExternal") != null) {
      isCanUseWriteExternal = (boolean) map.get("isCanUseWriteExternal");
    }
    boolean isCanUseWifiState = false;

    if (map.get("isCanUseWifiState") != null) {
      isCanUseWifiState = (boolean) map.get("isCanUseWifiState");
    }
    String devImei = "";
    if (map.get("devImei") != null) {
      devImei = (String) map.get("devImei");
    }

    String devOaid = "";
    if (map.get("devOaid") != null) {
      devOaid = (String) map.get("devOaid");
    }

    boolean location = false;
    if (map.get("location") != null) {
      location = (boolean) map.get("location");
    }

    boolean ttLocation = false;
    if (map.get("ttLocation") != null) {
      ttLocation = (boolean) map.get("ttLocation");
    }

    //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
    PackageManager packageManager = context.getPackageManager();
    Context applicationContext = context.getApplicationContext();
    PackageInfo pkgInfo = null;
    try {
      pkgInfo = packageManager.getPackageInfo(applicationContext.getPackageName(), 0);
      //获取应用名
      String appName = pkgInfo.applicationInfo.loadLabel(packageManager).toString();
      return new TTAdConfig.Builder()
          .appId(appId) //必填 ，不能为空   //5001121测试
          .appName(appName) //必填，不能为空
          .openAdnTest(debug)//开启第三方ADN测试时需要设置为true，会每次重新拉去最新配置，release 包情况下必须关闭.默认false
          .isPanglePaid(paid)//是否为费用户
          .setPublisherDid(getAndroidId(context)) //用户自定义device_id
          .openDebugLog(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
          .usePangleTextureView(
              true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
          .setPangleTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
          .allowPangleShowNotify(allowShowNotify) //是否允许sdk展示通知栏提示
          .allowPangleShowPageWhenScreenLock(allowShowPageWhenScreenLock) //是否在锁屏场景支持展示广告落地页
          .setPangleDirectDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI,
              TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
          .needPangleClearTaskReset()//特殊机型过滤，部分机型出现包解析失败问题（大部分是三星）。参数取android.os.Build.MODEL
          .setPrivacyConfig(privacyConfig)
          .build();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getAndroidId(Context context) {
    String androidId = null;
    try {
      androidId =
          Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return androidId;
  }

  public static void loadRewardVideoAd(@NotNull TTRewardAd mttRewardAd,
      final Activity activity, final MethodChannel.Result result) {

    mttRewardAd.showRewardAd(activity, new TTRewardedAdListener() {
      boolean isVerify = false;

      /**
       * 广告的展示回调 每个广告仅回调一次
       */
      public void onRewardedAdShow() {

      }

      /**
       * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
       * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
       * @param adError showFail的具体原因
       */
      @Override
      public void onRewardedAdShowFail(AdError adError) {
        MessageUtils.postVerifyMessage(result, adError.code, adError.message, isVerify);
      }

      /**
       * 注意Admob的激励视频不会回调该方法
       */
      @Override
      public void onRewardClick() {
      }

      /**
       * 广告关闭的回调
       */
      public void onRewardedAdClosed() {
        PangleResult pangleResult = new PangleResult();
        pangleResult.code = 0;
        pangleResult.message = "success";
        pangleResult.verify = isVerify;
        MessageUtils.postCustomMessage(result, pangleResult);
      }

      /**
       * 视频播放完毕的回调 Admob广告不存在该回调
       */
      public void onVideoComplete() {
      }

      /**
       * 1、视频播放失败的回调
       */
      public void onVideoError() {
        MessageUtils.postVerifyMessage(result, -1, "error", isVerify);
      }

      /**
       * 激励视频播放完毕，验证是否有效发放奖励的回调
       */
      public void onRewardVerify(RewardItem rewardItem) {
        if (rewardItem != null) {
          isVerify = rewardItem.rewardVerify();
        }
      }

      /**
       * - Mintegral GDT Admob广告不存在该回调
       */
      @Override
      public void onSkippedVideo() {
        MessageUtils.postVerifyMessage(result, -1, "skip", isVerify);
      }
    });
  }

  public static void loadInterstitialAd(@NotNull final TTInterstitialAd mInterstitialAd,
      @Nullable AdSlot interstitialAdSlot
      , final Activity mContext, final MethodChannel.Result result) {
    mInterstitialAd.loadAd(interstitialAdSlot, new TTInterstitialAdLoadCallback() {
      @Override
      public void onInterstitialLoadFail(AdError adError) {
        MessageUtils.postSimpleMessage(result, adError.code, adError.message);
      }

      @Override
      public void onInterstitialLoad() {
        mInterstitialAd.setTTAdInterstitialListener(new TTInterstitialAdListener() {

          @Override
          public void onInterstitialShow() {
          }

          /**
           * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
           * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
           * @param adError showFail的具体原因
           */
          @Override
          public void onInterstitialShowFail(AdError adError) {
            MessageUtils.postSimpleMessage(result, adError.code, adError.message);
          }

          /**
           * 广告被点击
           */
          @Override
          public void onInterstitialAdClick() {

          }

          /**
           * 关闭广告
           */
          @Override
          public void onInterstitialClosed() {

          }

          /**
           * 当广告打开浮层时调用，如打开内置浏览器、内容展示浮层，一般发生在点击之后
           * 常常在onAdLeftApplication之前调用
           */
          @Override
          public void onAdOpened() {

          }

          /**
           * 此方法会在用户点击打开其他应用（例如 Google Play）时
           * 于 onAdOpened() 之后调用，从而在后台运行当前应用。
           */
          @Override
          public void onAdLeftApplication() {

          }
        });
        mInterstitialAd.showAd(mContext);
      }
    });
  }

  public static void loadFullVideoAd(@NotNull final TTFullVideoAd mTTFullVideoAd,
      @Nullable AdSlot fullVideoAdSlot, PangleLoadingType loadingType, final Activity activity,
      final MethodChannel.Result result) {
    if (PangleLoadingType.preload_only == loadingType) {
      mTTFullVideoAd.loadFullAd(fullVideoAdSlot, new TTFullVideoAdLoadCallback() {

        @Override
        public void onFullVideoLoadFail(AdError adError) {
          MessageUtils.postSimpleMessage(result, adError.code, adError.message);
        }

        @Override
        public void onFullVideoAdLoad() {

        }

        @Override
        public void onFullVideoCached() {

        }
      });
    } else {
      mTTFullVideoAd.loadFullAd(fullVideoAdSlot, new TTFullVideoAdLoadCallback() {
        @Override
        public void onFullVideoLoadFail(AdError adError) {
          MessageUtils.postSimpleMessage(result, adError.code, adError.message);
        }

        @Override
        public void onFullVideoAdLoad() {
          mTTFullVideoAd.showFullAd(activity, listenerFull(result));
        }

        @Override
        public void onFullVideoCached() {
          mTTFullVideoAd.showFullAd(activity, listenerFull(result));
        }
      });
    }
  }

  public static TTFullVideoAdListener listenerFull(final MethodChannel.Result result) {

    return new TTFullVideoAdListener() {

      @Override
      public void onFullVideoAdShow() {

      }

      /**
       * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
       * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
       * @param adError showFail的具体原因
       */
      @Override
      public void onFullVideoAdShowFail(AdError adError) {
        MessageUtils.postSimpleMessage(result, adError.code, adError.message);
      }

      @Override
      public void onFullVideoAdClick() {
      }

      /**
       * 备注：相关回调说明 缺失onFullVideoAdShow()回调的有： 无
       * 缺失onFullVideoAdClick()回调的有： unity类型广告 缺失onFullVideoAdClosed()回调的有：无
       * 缺失onVideoComplete()回调的有： admob、baidu类型广告 缺失onVideoError()回调的有：admob、baidu、mintegral类型广告
       * 缺失onSkippedVideo()回调的有：admob、sigmob、baidu、mintegral、gdt类型广告
       */
      @Override
      public void onFullVideoAdClosed() {
        PangleResult pangleResult = new PangleResult();
        pangleResult.code = 0;
        pangleResult.message = "success";
        MessageUtils.postCustomMessage(result, pangleResult);
      }

      @Override
      public void onVideoComplete() {
      }

      /**
       * 1、视频播放失败的回调 - Mintegral GDT Admob广告不存在该回调；
       */
      @Override
      public void onVideoError() {

      }

      @Override
      public void onSkippedVideo() {
        MessageUtils.postSimpleMessage(result, -1, "skip");
      }
    };
  }

  public static void loadFeedListAd(@NotNull final TTUnifiedNativeAd mTTAdNative,
      @NotNull AdSlot adSlot, final MethodChannel.Result result) {

    mTTAdNative.loadAd(adSlot, new TTNativeAdLoadCallback() {
      @Override
      public void onAdLoaded(List<TTNativeAd> ads) {
        if (ads == null || ads.isEmpty()) {
          MessageUtils.postSimpleMessage(result, -1, "ads = null");
          return;
        }
        ArrayList<String> datas =
            (ArrayList<String>) PangleAdManager.Companion.getShared().setExpressAdV2(ads);
        PangleResult pangleResult = new PangleResult();
        pangleResult.code = 0;
        pangleResult.count = ads.size();
        pangleResult.data = datas;
        MessageUtils.postCustomMessage(result, pangleResult);
      }

      @Override
      public void onAdLoadedFial(AdError adError) {
        MessageUtils.postVerifyMessage(result, adError.code, adError.message, false);
      }
    });
  }
}
