package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.TTNetworkRequestInfo
import com.bytedance.msdk.api.splash.TTSplashAd
import com.bytedance.msdk.api.splash.TTSplashAdListener
import com.bytedance.msdk.api.splash.TTSplashAdLoadCallback
import com.bytedance.sdk.openadsdk.TTAdNative
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.util.asMap
import io.github.nullptrx.pangleflutter.v2.TTAdSlotManager

class FlutterSplashView(val context: Context, messenger: BinaryMessenger, val id: Int, params: Map<String, Any?>) : PlatformView, MethodChannel.MethodCallHandler, TTAdNative.SplashAdListener {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_splashview_$id")
  private val container: FrameLayout
  private var hideSkipButton = false

  init {
    methodChannel.setMethodCallHandler(this)
    container = FrameLayout(context)
    val slotId = params["slotId"] as? String

    Toast.makeText(context, slotId, Toast.LENGTH_LONG).show()

    if (slotId != null) {
      val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
      val tolerateTimeout = params["tolerateTimeout"] as Double?
      hideSkipButton = params["hideSkipButton"] as? Boolean ?: false

      val imgArgs: Map<String, Int?> = params["imageSize"]?.asMap() ?: mapOf()
      val w: Int = imgArgs["width"] ?: 1080
      val h: Int = imgArgs["height"] ?: 1920

      /**
       * 注：每次加载开屏广告的时候需要新建一个TTSplashAd，否则可能会出现广告填充问题
       * （ 例如：mTTSplashAd = new TTSplashAd(this, mAdUnitId);）
       */
      var mTTSplashAd = TTSplashAd(context as Activity?, slotId)
      mTTSplashAd.setTTAdSplashListener(object : TTSplashAdListener {
        override fun onAdClicked() {
          postMessage("onClick")
        }

        override fun onAdShow() {
          postMessage("onShow")
        }

        /**
         * show失败回调。如果show时发现无可用广告（比如广告过期），会触发该回调。
         * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
         * @param adError showFail的具体原因
         */
        override fun onAdShowFail(adError: AdError) {
          postMessage("onError", mapOf("message" to adError.message, "code" to adError.code))
        }

        override fun onAdSkip() {
          postMessage("onSkip")
        }

        override fun onAdDismiss() {
          postMessage("onTimeOver")
        }
      })
      //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档

      val adSlot = TTAdSlotManager.getSplashAdSlot(w, h, isSupportDeepLink);


      //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
      mTTSplashAd.loadAd(adSlot,object : TTSplashAdLoadCallback {
        override fun onSplashAdLoadFail(adError: AdError) {
          postMessage("onError", mapOf("message" to adError.message, "code" to adError.code))
        }

        override fun onSplashAdLoadSuccess() {
          if (mTTSplashAd != null) {
            mTTSplashAd.showAd(container)
          }
        }

        override fun onAdLoadTimeout() {
        }
      }, 3000)
    }
  }

  override fun getView(): View {
    return container
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    container.removeAllViews()
  }

  override fun onError(code: Int, message: String?) {
    postMessage("onError", mapOf("message" to message, "code" to code))
  }

  override fun onTimeout() {
    postMessage("onError", mapOf("message" to "timeout", "code" to -1))
  }

  override fun onSplashAdLoad(p0: com.bytedance.sdk.openadsdk.TTSplashAd?) {
    TODO("Not yet implemented")
  }


  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    when (call.method) {
      else -> result.notImplemented()
    }
  }

  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    methodChannel.invokeMethod(method, arguments)
  }
}

