package io.github.nullptrx.pangleflutter

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.fullVideo.TTFullVideoAd
import com.bytedance.msdk.api.interstitial.TTInterstitialAd
import com.bytedance.msdk.api.nativeAd.TTUnifiedNativeAd
import com.bytedance.msdk.api.reward.TTRewardAd
import com.bytedance.msdk.api.reward.TTRewardedAdLoadCallback
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.github.nullptrx.pangleflutter.common.PangleLoadingType
import io.github.nullptrx.pangleflutter.util.MessageUtils
import io.github.nullptrx.pangleflutter.util.asMap
import io.github.nullptrx.pangleflutter.v2.TTAdManagerHolder
import io.github.nullptrx.pangleflutter.v2.TTAdSlotManager
import io.github.nullptrx.pangleflutter.view.BannerViewFactory
import io.github.nullptrx.pangleflutter.view.FeedViewFactory
import io.github.nullptrx.pangleflutter.view.NativeBannerViewFactory
import io.github.nullptrx.pangleflutter.view.SplashViewFactory

/** PangleFlutterPlugin */
class PangleFlutterPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
  companion object {
    const val kChannelName = "nullptrx.github.io/pangle"
  }

  private var methodChannel: MethodChannel? = null
  private var activity: Activity? = null
  private var context: Context? = null
  private var bannerViewFactory: BannerViewFactory? = null
  private var splashViewFactory: SplashViewFactory? = null
  private var feedViewFactory: FeedViewFactory? = null
  private val handler = Handler(Looper.getMainLooper())

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    feedViewFactory?.attachActivity(binding.activity)
    bannerViewFactory?.attachActivity(binding.activity)
    splashViewFactory?.attachActivity(binding.activity)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    feedViewFactory?.attachActivity(binding.activity)
    bannerViewFactory?.attachActivity(binding.activity)
    splashViewFactory?.attachActivity(binding.activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    feedViewFactory?.detachActivity()
    bannerViewFactory?.detachActivity()
    splashViewFactory?.detachActivity()
    activity = null
  }

  override fun onDetachedFromActivity() {
    feedViewFactory?.detachActivity()
    bannerViewFactory?.detachActivity()
    splashViewFactory?.detachActivity()
    activity = null
  }

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    binding.binaryMessenger

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

    splashViewFactory = SplashViewFactory(binding.binaryMessenger)
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
    when (call.method) {
      "getSdkVersion" -> {

      }
      "init" -> {
        TTAdManagerHolder.init(activity!!, call.arguments.asMap() ?: mapOf(), result)
      }

      "requestPermissionIfNecessary" -> {
      }
      "loadSplashAd" -> {
        /**
        val slotId = call.argument<String>("slotId")!!
        val isExpress = call.argument<Boolean>("isExpress") ?: false
        val tolerateTimeout = call.argument<Double>("tolerateTimeout") ?: 3000
        val hideSkipButton = call.argument<Boolean>("hideSkipButton")
        val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink") ?: true
        val imgSize = TTSize(1080, 1920)
        val splashAdSlot = TTAdSlotManager.getSplashAdSlot(imgSize.width, imgSize.height, isSupportDeepLink);
        var mTTSplashAd = TTSplashAd(activity, slotId)
        TTAdManagerHolder.loadSplashAdV2(mTTSplashAd,splashAdSlot, FLTSplashAdV2(mTTSplashAd,hideSkipButton, activity) {
        result.success(it)
        }, tolerateTimeout.toInt())
         */
      }
      "loadRewardedVideoAd" -> {
        val loadingTypeIndex = call.argument<Int>("loadingType") ?: 0
        val loadingType = PangleLoadingType.values()[loadingTypeIndex] //普通，预加载，显示
        val slotId = call.argument<String>("slotId")!!
        val mttRewardAd = TTRewardAd(activity, slotId)
        val rewardAdSlot = TTAdSlotManager.getRewardAdSlot(call)
        if (PangleLoadingType.preload_only == loadingType) {
          mttRewardAd.loadRewardAd(rewardAdSlot, object : TTRewardedAdLoadCallback {
            override fun onRewardVideoLoadFail(adError: AdError) {
              handler.post {
                MessageUtils.postVerifyMessage(result, adError.code, adError.message, false)
              }
            }

            override fun onRewardVideoAdLoad() {

            }

            override fun onRewardVideoCached() {

            }
          })
        } else {
          mttRewardAd.loadRewardAd(rewardAdSlot, object : TTRewardedAdLoadCallback {
            override fun onRewardVideoLoadFail(adError: AdError) {
              handler.post {
                MessageUtils.postVerifyMessage(result, adError.code, adError.message, false)
              }
            }

            override fun onRewardVideoAdLoad() {
//              Log.d("pangle", "onRewardVideoAdLoad")
            }

            override fun onRewardVideoCached() {
//              Log.d("pangle", "onRewardVideoCached")
              if (mttRewardAd.isReady) {
                TTAdManagerHolder.loadRewardVideoAd(mttRewardAd, activity, result)
              }
            }
          })
        }
      }

      "loadBannerAd" -> {

      }

      "loadFeedAd" -> {
        val slotId = call.argument<String>("slotId")!!
        val mTTAdNative = TTUnifiedNativeAd(activity, slotId) //模板视频
        val adSlot =
          TTAdSlotManager.getFeedListAdSlot(call)
        TTAdManagerHolder.loadFeedListAd(mTTAdNative, adSlot, result)
      }

      "removeFeedAd" -> {
        val feedIds = call.arguments<List<String>>()
        var count = 0
        for (feedId in feedIds) {
          val success = PangleAdManager.shared.removeExpressAdV2(feedId)
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
        val mInterstitialAd = TTInterstitialAd(activity, slotId)
        val interstitialAdSlot = TTAdSlotManager.getInterstitialAdAdSlot(w, h, isSupportDeepLink)
        TTAdManagerHolder.loadInterstitialAd(mInterstitialAd, interstitialAdSlot, activity, result)
      }

      "loadFullscreenVideoAd" -> {
        val loadingTypeIndex = call.argument<Int>("loadingType") ?: 0  //是否预加载，暂未处理
        val loadingType = PangleLoadingType.values()[loadingTypeIndex]
        val slotId = call.argument<String>("slotId")!!
        val mTTFullVideoAd = TTFullVideoAd(activity, slotId)
        val fullVideoAdSlot = TTAdSlotManager.getFullVideoAdSlot(call)
        TTAdManagerHolder.loadFullVideoAd(
          mTTFullVideoAd,
          fullVideoAdSlot,
          loadingType,
          activity,
          result
        )
      }
      "setThemeStatus" -> {

      }
      "getThemeStatus" -> {
      }

      else -> result.notImplemented()
    }

  }

}
