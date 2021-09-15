package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.TTNetworkRequestInfo
import com.bytedance.msdk.api.splash.TTSplashAd
import com.bytedance.msdk.api.splash.TTSplashAdListener
import com.bytedance.msdk.api.splash.TTSplashAdLoadCallback
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.common.TTSize
import io.github.nullptrx.pangleflutter.util.asMap
import io.github.nullptrx.pangleflutter.v2.TTAdSlotManager

class FlutterSplashView(val context: Activity, messenger: BinaryMessenger, val id: Int, params: Map<String, Any?>) : PlatformView, MethodChannel.MethodCallHandler {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_splashview_$id")
  private val container: FrameLayout
  private var hideSkipButton = false

  init {
    methodChannel.setMethodCallHandler(this)
    container = FrameLayout(context)
    val slotId = params["slotId"] as? String
    if (slotId != null) {
      val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
      val tolerateTimeout = params["tolerateTimeout"] as? Double ?: 3
      hideSkipButton = params["hideSkipButton"] as? Boolean ?: false
      val imgArgs: Map<String, Int?> = params["imageSize"]?.asMap() ?: mapOf()
      val w: Int = imgArgs["width"] ?: 1080
      val h: Int = imgArgs["height"] ?: 1920

      var mTTSplashAd = TTSplashAd(context , slotId)
      mTTSplashAd.setTTAdSplashListener(object : TTSplashAdListener {
        override fun onAdClicked() {
          postMessage("onClick")
        }

        override fun onAdShow() {
          postMessage("onShow")
        }

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
      var ttNetworkRequestInfo : TTNetworkRequestInfo;
      ttNetworkRequestInfo = PangleNetworkRequestInfo("5156773", "887562558");
      //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
      mTTSplashAd.loadAd(adSlot,ttNetworkRequestInfo,object : TTSplashAdLoadCallback {
        override fun onSplashAdLoadFail(adError: AdError) {
          postMessage("onError", mapOf("message" to adError.message + " adMessgaeDetail=" +  mTTSplashAd.adLoadInfoList, "code" to adError.code))
        }

        override fun onSplashAdLoadSuccess() {
          if (mTTSplashAd != null) {
            mTTSplashAd.showAd(container)
          }
        }

        override fun onAdLoadTimeout() {
        }
      }, tolerateTimeout.toInt())
    }
  }

  override fun getView(): View {
    return container
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    container.removeAllViews()
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

