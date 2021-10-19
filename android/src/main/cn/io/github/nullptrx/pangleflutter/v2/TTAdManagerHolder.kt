package io.github.nullptrx.pangleflutter.v2

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.provider.Settings.System
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.AdSlot
import com.bytedance.msdk.api.TTAdConfig
import com.bytedance.msdk.api.TTAdConfig.Builder
import com.bytedance.msdk.api.TTAdConstant
import com.bytedance.msdk.api.TTMediationAdSdk
import com.bytedance.msdk.api.TTPrivacyConfig
import com.bytedance.msdk.api.fullVideo.TTFullVideoAd
import com.bytedance.msdk.api.fullVideo.TTFullVideoAdListener
import com.bytedance.msdk.api.fullVideo.TTFullVideoAdLoadCallback
import com.bytedance.msdk.api.interstitial.TTInterstitialAd
import com.bytedance.msdk.api.interstitial.TTInterstitialAdListener
import com.bytedance.msdk.api.interstitial.TTInterstitialAdLoadCallback
import com.bytedance.msdk.api.nativeAd.TTNativeAd
import com.bytedance.msdk.api.nativeAd.TTNativeAdLoadCallback
import com.bytedance.msdk.api.nativeAd.TTUnifiedNativeAd
import com.bytedance.msdk.api.reward.RewardItem
import com.bytedance.msdk.api.reward.TTRewardAd
import com.bytedance.msdk.api.reward.TTRewardedAdListener
import io.flutter.plugin.common.MethodChannel.Result
import io.github.nullptrx.pangleflutter.PangleAdManager.Companion.shared
import io.github.nullptrx.pangleflutter.bean.PangleResult
import io.github.nullptrx.pangleflutter.common.PangleLoadingType
import io.github.nullptrx.pangleflutter.common.PangleLoadingType.preload_only
import io.github.nullptrx.pangleflutter.util.MessageUtils.postCustomMessage
import io.github.nullptrx.pangleflutter.util.MessageUtils.postSimpleMessage
import io.github.nullptrx.pangleflutter.util.MessageUtils.postVerifyMessage
import java.util.ArrayList

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
object TTAdManagerHolder {
  private var sInit = false
  var privacyConfig: TTPrivacyConfig = object : TTPrivacyConfig() {
    override fun isLimitPersonalAds(): Boolean {
      return true
    }

    override fun isCanUsePhoneState(): Boolean {
      return false
    }

    override fun isCanUseLocation(): Boolean {
      return false
    }
  }

  /**
   * @param context 上下文
   * @param map 桥接参数
   */
  fun init(
    context: Context,
    map: Map<String, Any?>,
    result: Result
  ) {
    doInit(context, map, result)
  }

  //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
  private fun doInit(
    context: Context,
    map: Map<String, Any?>,
    result: Result
  ) {
    if (!sInit) {
      sInit = true
      val ttAdConfig = buildConfig(context, map)
      if (ttAdConfig == null) {
        postSimpleMessage(result, -1, "init error")
        return
      }
      TTMediationAdSdk.initialize(context, buildConfig(context, map))
    }
    postSimpleMessage(result, 0, "init success")
  }

  private fun buildConfig(
    context: Context,
    map: Map<String, Any?>
  ): TTAdConfig? {
    var appId: String? = ""
    if (map["appId"] != null) {
      appId = map["appId"] as String?
    }
    var debug = false
    if (map["debug"] != null) {
      debug = map["debug"] as Boolean
    }
    var allowShowNotify = false
    if (map["allowShowNotify"] != null) {
      allowShowNotify = map["allowShowNotify"] as Boolean
    }
    var allowShowPageWhenScreenLock = false
    if (map["allowShowPageWhenScreenLock"] != null) {
      allowShowPageWhenScreenLock = map["allowShowPageWhenScreenLock"] as Boolean
    }
    var paid = false
    if (map["paid"] != null) {
      paid = map["paid"] as Boolean
    }
    //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
    val packageManager = context.packageManager
    val applicationContext = context.applicationContext
    val pkgInfo: PackageInfo?
    try {
      pkgInfo = packageManager.getPackageInfo(applicationContext.packageName, 0)
      //获取应用名
      val appName = pkgInfo.applicationInfo.loadLabel(packageManager).toString()
      return Builder()
        .appId(appId) //必填 ，不能为空   //5001121测试
        .appName(appName) //必填，不能为空
        .openAdnTest(debug) //开启第三方ADN测试时需要设置为true，会每次重新拉去最新配置，release 包情况下必须关闭.默认false
        .isPanglePaid(paid) //是否为费用户
        .setPublisherDid(getAndroidId(context)) //用户自定义device_id
        .openDebugLog(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
        .usePangleTextureView(
          true
        ) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
        .setPangleTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
        .allowPangleShowNotify(allowShowNotify) //是否允许sdk展示通知栏提示
        .allowPangleShowPageWhenScreenLock(allowShowPageWhenScreenLock) //是否在锁屏场景支持展示广告落地页
        .setPangleDirectDownloadNetworkType(
          TTAdConstant.NETWORK_STATE_WIFI,
          TTAdConstant.NETWORK_STATE_3G
        ) //允许直接下载的网络状态集合
        .needPangleClearTaskReset() //特殊机型过滤，部分机型出现包解析失败问题（大部分是三星）。参数取android.os.Build.MODEL
        .setPrivacyConfig(privacyConfig)
        .build()
    } catch (e: NameNotFoundException) {
      e.printStackTrace()
    }
    return null
  }

  fun getAndroidId(context: Context): String? {
    var androidId: String? = null
    try {
      androidId = System.getString(context.contentResolver, System.ANDROID_ID)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return androidId
  }

  fun loadRewardVideoAd(
    mttRewardAd: TTRewardAd,
    activity: Activity?,
    result: Result?
  ) {
    mttRewardAd.showRewardAd(activity, object : TTRewardedAdListener {
      var isVerify = false

      /**
       * 广告的展示回调 每个广告仅回调一次
       */
      override fun onRewardedAdShow() {}

      /**
       * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
       * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
       * @param adError showFail的具体原因
       */
      override fun onRewardedAdShowFail(adError: AdError) {
        postVerifyMessage(result, adError.code, adError.message, isVerify)
      }

      /**
       * 注意Admob的激励视频不会回调该方法
       */
      override fun onRewardClick() {}

      /**
       * 广告关闭的回调
       */
      override fun onRewardedAdClosed() {
        val pangleResult = PangleResult()
        pangleResult.code = 0
        pangleResult.message = "success"
        pangleResult.verify = isVerify
        postCustomMessage(result, pangleResult)
      }

      /**
       * 视频播放完毕的回调 Admob广告不存在该回调
       */
      override fun onVideoComplete() {}

      /**
       * 1、视频播放失败的回调
       */
      override fun onVideoError() {
        postVerifyMessage(result, -1, "error", isVerify)
      }

      /**
       * 激励视频播放完毕，验证是否有效发放奖励的回调
       */
      override fun onRewardVerify(rewardItem: RewardItem) {
        if (rewardItem != null) {
          isVerify = rewardItem.rewardVerify()
        }
      }

      /**
       * - Mintegral GDT Admob广告不存在该回调
       */
      override fun onSkippedVideo() {
        postVerifyMessage(result, -1, "skip", isVerify)
      }
    })
  }

  fun loadInterstitialAd(
    mInterstitialAd: TTInterstitialAd,
    interstitialAdSlot: AdSlot?,
    mContext: Activity?,
    result: Result?
  ) {
    mInterstitialAd.loadAd(interstitialAdSlot, object : TTInterstitialAdLoadCallback {
      override fun onInterstitialLoadFail(adError: AdError) {
        postSimpleMessage(result, adError.code, adError.message)
      }

      override fun onInterstitialLoad() {
        mInterstitialAd.setTTAdInterstitialListener(object : TTInterstitialAdListener {
          override fun onInterstitialShow() {}

          /**
           * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
           * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
           * @param adError showFail的具体原因
           */
          override fun onInterstitialShowFail(adError: AdError) {
            postSimpleMessage(result, adError.code, adError.message)
          }

          /**
           * 广告被点击
           */
          override fun onInterstitialAdClick() {}

          /**
           * 关闭广告
           */
          override fun onInterstitialClosed() {}

          /**
           * 当广告打开浮层时调用，如打开内置浏览器、内容展示浮层，一般发生在点击之后
           * 常常在onAdLeftApplication之前调用
           */
          override fun onAdOpened() {}

          /**
           * 此方法会在用户点击打开其他应用（例如 Google Play）时
           * 于 onAdOpened() 之后调用，从而在后台运行当前应用。
           */
          override fun onAdLeftApplication() {}
        })
        mInterstitialAd.showAd(mContext)
      }
    })
  }

  fun loadFullVideoAd(
    mTTFullVideoAd: TTFullVideoAd,
    fullVideoAdSlot: AdSlot?,
    loadingType: PangleLoadingType,
    activity: Activity?,
    result: Result?
  ) {
    if (preload_only === loadingType) {
      mTTFullVideoAd.loadFullAd(fullVideoAdSlot, object : TTFullVideoAdLoadCallback {
        override fun onFullVideoLoadFail(adError: AdError) {
          postSimpleMessage(result, adError.code, adError.message)
        }

        override fun onFullVideoAdLoad() {}
        override fun onFullVideoCached() {
        }
      })
    } else {
      var isSuccess = false
      mTTFullVideoAd.loadFullAd(fullVideoAdSlot, object : TTFullVideoAdLoadCallback {
        override fun onFullVideoLoadFail(adError: AdError) {
          postSimpleMessage(result, adError.code, adError.message)
        }

        override fun onFullVideoAdLoad() {
          if (mTTFullVideoAd.isReady && !isSuccess) {
            isSuccess = true
            mTTFullVideoAd.showFullAd(activity, listenerFull(result))
          }
        }

        override fun onFullVideoCached() {
          if (mTTFullVideoAd.isReady && !isSuccess) {
            isSuccess = true
            mTTFullVideoAd.showFullAd(activity, listenerFull(result))
          }
        }
      })
    }
  }

  fun listenerFull(result: Result?): TTFullVideoAdListener {
    return object : TTFullVideoAdListener {
      override fun onFullVideoAdShow() {}

      /**
       * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
       * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
       * @param adError showFail的具体原因
       */
      override fun onFullVideoAdShowFail(adError: AdError) {
        postSimpleMessage(result, adError.code, adError.message)
      }

      override fun onFullVideoAdClick() {}

      /**
       * 备注：相关回调说明 缺失onFullVideoAdShow()回调的有： 无
       * 缺失onFullVideoAdClick()回调的有： unity类型广告 缺失onFullVideoAdClosed()回调的有：无
       * 缺失onVideoComplete()回调的有： admob、baidu类型广告 缺失onVideoError()回调的有：admob、baidu、mintegral类型广告
       * 缺失onSkippedVideo()回调的有：admob、sigmob、baidu、mintegral、gdt类型广告
       */
      override fun onFullVideoAdClosed() {
        val pangleResult = PangleResult()
        pangleResult.code = 0
        pangleResult.message = "success"
        postCustomMessage(result, pangleResult)
      }

      override fun onVideoComplete() {}

      /**
       * 1、视频播放失败的回调 - Mintegral GDT Admob广告不存在该回调；
       */
      override fun onVideoError() {}
      override fun onSkippedVideo() {
        postSimpleMessage(result, -1, "skip")
      }
    }
  }

  fun loadFeedListAd(
    mTTAdNative: TTUnifiedNativeAd,
    adSlot: AdSlot,
    result: Result?
  ) {
    mTTAdNative.loadAd(adSlot, object : TTNativeAdLoadCallback {
      override fun onAdLoaded(ads: List<TTNativeAd>) {
        if (ads == null || ads.isEmpty()) {
          postSimpleMessage(result, -1, "ads = null")
          return
        }
        val datas = shared.setExpressAdV2(ads) as ArrayList<String?>
        val pangleResult = PangleResult()
        pangleResult.code = 0
        pangleResult.count = ads.size
        pangleResult.data = datas
        postCustomMessage(result, pangleResult)
      }

      override fun onAdLoadedFial(adError: AdError) {
        postVerifyMessage(result, adError.code, adError.message, false)
      }
    })
  }
}
