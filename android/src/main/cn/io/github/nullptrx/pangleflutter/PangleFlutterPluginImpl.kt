package io.github.nullptrx.pangleflutter

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.fullVideo.TTFullVideoAd
import com.bytedance.msdk.api.interstitial.TTInterstitialAd
import com.bytedance.msdk.api.nativeAd.TTUnifiedNativeAd
import com.bytedance.msdk.api.reward.TTRewardAd
import com.bytedance.msdk.api.reward.TTRewardedAdLoadCallback
import com.bytedance.msdk.api.splash.TTSplashAd
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.github.nullptrx.pangleflutter.common.PangleLoadingType
import io.github.nullptrx.pangleflutter.common.PangleOrientation
import io.github.nullptrx.pangleflutter.common.TTSize
import io.github.nullptrx.pangleflutter.common.TTSizeF
import io.github.nullptrx.pangleflutter.util.asMap
import io.github.nullptrx.pangleflutter.v2.TTAdManagerHolder
import io.github.nullptrx.pangleflutter.v2.TTAdSlotManager
import io.github.nullptrx.pangleflutter.v2.delegate.FLTSplashAdV2
import io.github.nullptrx.pangleflutter.view.BannerViewFactory
import io.github.nullptrx.pangleflutter.view.FeedViewFactory
import io.github.nullptrx.pangleflutter.view.NativeBannerViewFactory
import io.github.nullptrx.pangleflutter.view.SplashViewFactory

/** PangleFlutterPlugin */
open class PangleFlutterPluginImpl : FlutterPlugin, MethodCallHandler, ActivityAware {
  companion object {
    val kDefaultBannerAdCount = 3
    val kDefaultFeedAdCount = 3
    val kChannelName = "nullptrx.github.io/pangle"

    @JvmStatic
    fun registerWith(registrar: Registrar) {

      PangleFlutterPluginImpl().apply {

        val messenger = registrar.messenger()
        val activity = registrar.activity()
        this.activity = activity
        this.context = registrar.context().applicationContext

        methodChannel = MethodChannel(messenger, kChannelName)
        methodChannel?.setMethodCallHandler(this)

        bannerViewFactory = BannerViewFactory(messenger)
        registrar.platformViewRegistry().registerViewFactory(
          "nullptrx.github.io/pangle_bannerview",
          bannerViewFactory
        )
        feedViewFactory = FeedViewFactory(messenger)
        registrar.platformViewRegistry().registerViewFactory(
          "nullptrx.github.io/pangle_feedview",
          feedViewFactory
        )

        val splashViewFactory = SplashViewFactory(messenger)
        registrar.platformViewRegistry().registerViewFactory(
          "nullptrx.github.io/pangle_splashview",
          splashViewFactory
        )

        val nativeBannerViewFactory = NativeBannerViewFactory(messenger)
        registrar.platformViewRegistry().registerViewFactory(
          "nullptrx.github.io/pangle_nativebannerview",
          nativeBannerViewFactory
        )

        feedViewFactory?.attachActivity(activity)
        bannerViewFactory?.attachActivity(activity)

      }
    }
  }

  private var methodChannel: MethodChannel? = null
  private var activity: Activity? = null
  private var context: Context? = null
  private var bannerViewFactory: BannerViewFactory? = null
  private var feedViewFactory: FeedViewFactory? = null
  private val handler = Handler(Looper.getMainLooper())

  private val result: MethodChannel.Result? = null

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    feedViewFactory?.attachActivity(binding.activity)
    bannerViewFactory?.attachActivity(binding.activity)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    feedViewFactory?.attachActivity(binding.activity)
    bannerViewFactory?.attachActivity(binding.activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    feedViewFactory?.detachActivity()
    bannerViewFactory?.detachActivity()
    activity = null
  }

  override fun onDetachedFromActivity() {
    feedViewFactory?.detachActivity()
    bannerViewFactory?.detachActivity()
    activity = null
  }

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    context = binding.applicationContext

    methodChannel = MethodChannel(binding.binaryMessenger, kChannelName)
    methodChannel?.setMethodCallHandler(this)

    bannerViewFactory = BannerViewFactory(binding.binaryMessenger)
    binding.platformViewRegistry.registerViewFactory(
      "nullptrx.github.io/pangle_bannerview",
      bannerViewFactory
    )
    feedViewFactory = FeedViewFactory(binding.binaryMessenger)
    binding.platformViewRegistry.registerViewFactory(
      "nullptrx.github.io/pangle_feedview",
      feedViewFactory
    )

    val splashViewFactory = SplashViewFactory(binding.binaryMessenger)
    binding.platformViewRegistry.registerViewFactory(
      "nullptrx.github.io/pangle_splashview",
      splashViewFactory
    )

    val nativeBannerViewFactory = NativeBannerViewFactory(binding.binaryMessenger)
    binding.platformViewRegistry.registerViewFactory(
      "nullptrx.github.io/pangle_nativebannerview",
      nativeBannerViewFactory
    )
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel?.setMethodCallHandler(null)
    methodChannel = null
  }



  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    activity ?: return
    val pangle = PangleAdManager.shared
    when (call.method) {
      "getSdkVersion" -> {
        val version = pangle.getSdkVersion()
        result.success(version)
      }
      "init" -> {
//        pangle.initialize(activity, call.arguments.asMap() ?: mapOf()) {
//          handler.post {
//            result.success(it)
//          }
//        }
        TTAdManagerHolder.init(activity,call.arguments.asMap() ?: mapOf());
        result.success(mapOf("code" to 0, "message" to ""))
      }

      "requestPermissionIfNecessary" -> {
        context?.also {
          pangle.requestPermissionIfNecessary(it)
        }
      }
      "loadSplashAd" -> {
        val slotId = call.argument<String>("slotId")!!
        val isExpress = call.argument<Boolean>("isExpress") ?: false
        val tolerateTimeout = call.argument<Double>("tolerateTimeout")
        val hideSkipButton = call.argument<Boolean>("hideSkipButton")
        val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true
        val imgSize = TTSize(1080, 1920)
        Toast.makeText(activity, slotId, Toast.LENGTH_LONG).show()
        val splashAdSlot = TTAdSlotManager.getSplashAdSlot(imgSize.width, imgSize.height, isSupportDeepLink);
        var mTTSplashAd = TTSplashAd(activity, slotId)

        Toast.makeText(context,"未提供获取splashView方法,暂缓添加",Toast.LENGTH_LONG).show()
        TTAdManagerHolder.loadSplashAdV2(mTTSplashAd,splashAdSlot, FLTSplashAdV2(hideSkipButton, activity) {
          result.success(it)
        }, 3000)
      }
      "loadRewardedVideoAd" -> {


        val loadingTypeIndex = call.argument<Int>("loadingType") ?: 0
        val loadingType = PangleLoadingType.values()[loadingTypeIndex]
        val slotId = call.argument<String>("slotId")!!
        /**
         * 注：每次加载激励视频广告的时候需要新建一个TTRewardAd，否则可能会出现广告填充问题
         * （ 例如：mttRewardAd = new TTRewardAd(this, adUnitId);）
         */
        val mttRewardAd = TTRewardAd(activity, slotId)
        val rewardAdSlot = TTAdSlotManager.getRewardAdSlot(0);

        //请求广告
        mttRewardAd.loadRewardAd(rewardAdSlot, object : TTRewardedAdLoadCallback {
          override fun onRewardVideoLoadFail(adError: AdError) {
          }

          override fun onRewardVideoAdLoad() {

            //在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
            //该方法直接展示广告，如果展示失败了（如过期），会回调onVideoError()
            //展示广告，并传入广告展示的场景
            TTAdManagerHolder.loadRewardVideoAdV2(mttRewardAd,rewardAdSlot,activity);

          }

          override fun onRewardVideoCached() {
            TTAdManagerHolder.loadRewardVideoAdV2(mttRewardAd,rewardAdSlot,activity);
          }
        })


//        if (PangleLoadingType.preload == loadingType || PangleLoadingType.normal == loadingType) {
//          val loadResult = pangle.showRewardedVideoAd(slotId, activity) {
//            if (PangleLoadingType.preload == loadingType) {
//              loadRewardedVideoAdOnly(call, PangleLoadingType.preload_only)
//            }
//            result.success(it)
//          }
//          if (!loadResult) {
//            loadRewardedVideoAdOnly(call, PangleLoadingType.normal, result)
//          }
//
//        } else {
//          loadRewardedVideoAdOnly(call, PangleLoadingType.preload_only, result)
//        }

      }

      "loadBannerAd" -> {
        val slotId = call.argument<String>("slotId")!!
        val count = call.argument<Int>("count") ?: kDefaultBannerAdCount
        val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true

        val expressArgs = call.argument<Map<String, Double>>("expressSize") ?: mapOf()
        val w: Float = expressArgs.getValue("width").toFloat()
        val h: Float = expressArgs.getValue("height").toFloat()
        val expressSize = TTSizeF(w, h)
        val adSlot =
          PangleAdSlotManager.getBannerAdSlot(slotId, expressSize, count, isSupportDeepLink)
        pangle.loadBanner2ExpressAd(adSlot) {
          result.success(it)
        }
      }

      "loadFeedAd" -> {


        val slotId = call.argument<String>("slotId")!!
        val count = call.argument<Int>("count") ?: kDefaultFeedAdCount
        val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true

        val expressArgs = call.argument<Map<String, Double>>("expressSize") ?: mapOf()
        val w: Float = expressArgs.getValue("width").toFloat()
        val h: Float = expressArgs.getValue("height").toFloat()
        val expressSize = TTSizeF(w, h)


        var mTTAdNative = TTUnifiedNativeAd(activity, slotId) //模板视频
        val adSlot =
                TTAdSlotManager.getFeedListAdSlot(0)

        TTAdManagerHolder.loadFeedListAdV2(mTTAdNative,adSlot,activity);
      }
      "removeFeedAd" -> {
        val feedIds = call.arguments<List<String>>()
        var count = 0
        for (feedId in feedIds) {
          val success = PangleAdManager.shared.removeExpressAd(feedId)
          if (success) {
            count++
          }
        }
        result.success(count)
      }

      "loadInterstitialAd" -> {
        val slotId = call.argument<String>("slotId")!!
        val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true

        val expressArgs = call.argument<Map<String, Double>>("expressSize") ?: mapOf()
        val w: Float = expressArgs.getValue("width").toFloat()
        val h: Float = expressArgs.getValue("height").toFloat()
        val expressSize = TTSizeF(w, h)


        //Context 必须传activity
        /**
         * 注：每次加载插屏广告的时候需要新建一个TTInterstitialAd，否则可能会出现广告填充问题
         * （ 例如：mInterstitialAd = new TTInterstitialAd(this, adUnitId);）
         */
        val mInterstitialAd = TTInterstitialAd(activity,slotId )
        val interstitialAdSlot = TTAdSlotManager.getInterstitialAdAdSlot();
        TTAdManagerHolder.loadInterstitialAdV2(mInterstitialAd,interstitialAdSlot,activity);
      }

      "loadFullscreenVideoAd" -> {

        val loadingTypeIndex = call.argument<Int>("loadingType") ?: 0
        val loadingType = PangleLoadingType.values()[loadingTypeIndex]
        val slotId = call.argument<String>("slotId")!!

        /**
         * 注：每次加载全屏视频广告的时候需要新建一个TTFullVideoAd，否则可能会出现广告填充问题
         * （ 例如：mTTFullVideoAd = new TTFullVideoAd(this, adUnitId);）
         */
        val mTTFullVideoAd = TTFullVideoAd(activity, slotId)
        val fullVideoAdSlot = TTAdSlotManager.getFullVideoAdSlot(0);
        TTAdManagerHolder.loadFullVideoAdV2(mTTFullVideoAd,fullVideoAdSlot,activity);
      }
      "setThemeStatus" -> {
        var theme: Int = call.arguments()
        pangle.setThemeStatus(theme)
        theme = pangle.getThemeStatus()
        result.success(theme)
      }
      "getThemeStatus" -> {
        val theme = pangle.getThemeStatus()
        result.success(theme)
      }

      else -> result.notImplemented()
    }

  }

  private fun loadRewardedVideoAdOnly(
    call: MethodCall,
    loadingType: PangleLoadingType,
    result: MethodChannel.Result? = null
  ) {

    val slotId = call.argument<String>("slotId")!!
    val userId = call.argument<String>("userId")
    val rewardName = call.argument<String>("rewardName")
    val rewardAmount = call.argument<Int>("rewardAmount")
    val extra = call.argument<String>("extra")
    val isVertical = call.argument<Boolean>("isVertical") ?: true
    val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true
    val expressArgs = call.argument<Map<String, Double>>("expressSize") ?: mapOf()
    val w: Float = expressArgs.getValue("width").toFloat()
    val h: Float = expressArgs.getValue("height").toFloat()
    val expressSize = TTSizeF(w, h)
    val adSlot = PangleAdSlotManager.getRewardVideoAdSlot(
      slotId,
      expressSize,
      userId,
      rewardName,
      rewardAmount,
      isVertical,
      isSupportDeepLink,
      extra
    )

    PangleAdManager.shared.loadRewardVideoAd(adSlot, activity, loadingType) {
      if (PangleLoadingType.preload_only == loadingType || PangleLoadingType.normal == loadingType) {
        result?.success(it)
      }
    }
  }

  private fun loadFullscreenVideoAdOnly(
    call: MethodCall,
    loadingType: PangleLoadingType,
    result: MethodChannel.Result? = null
  ) {
    val slotId = call.argument<String>("slotId")!!
    val orientationIndex = call.argument<Int>("orientation")
      ?: PangleOrientation.veritical.ordinal
    val orientation = PangleOrientation.values()[orientationIndex]
    val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true
    val expressArgs = call.argument<Map<String, Double>>("expressSize") ?: mapOf()
    val w: Float = expressArgs.getValue("width").toFloat()
    val h: Float = expressArgs.getValue("height").toFloat()
    val expressSize = TTSizeF(w, h)
    val adSlot = PangleAdSlotManager.getFullScreenVideoAdSlot(
      slotId,
      expressSize,
      orientation,
      isSupportDeepLink
    )

    PangleAdManager.shared.loadFullscreenVideoAd(adSlot, activity, loadingType) {
      if (PangleLoadingType.preload_only == loadingType || PangleLoadingType.normal == loadingType) {
        result?.success(it)
      }
    }
  }
}
