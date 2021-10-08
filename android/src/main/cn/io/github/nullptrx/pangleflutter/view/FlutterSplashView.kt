package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.os.CountDownTimer
import android.view.View
import android.widget.FrameLayout
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.TTMediationAdSdk
import com.bytedance.msdk.api.TTNetworkRequestInfo
import com.bytedance.msdk.api.TTSettingConfigCallback
import com.bytedance.msdk.api.splash.TTSplashAd
import com.bytedance.msdk.api.splash.TTSplashAdListener
import com.bytedance.msdk.api.splash.TTSplashAdLoadCallback
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.util.asMap
import io.github.nullptrx.pangleflutter.v2.TTAdSlotManager

class FlutterSplashView(val context: Activity, messenger: BinaryMessenger, val id: Int, params: Map<String, Any?>) : PlatformView, MethodChannel.MethodCallHandler {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_splashview_$id")
  private val container: FrameLayout
  private var mTTSplashAd: TTSplashAd? = null
  private var hideSkipButton = false
  private lateinit var countDownTimer: CountDownTimer
  private var isCompleted = false;

  override fun onFlutterViewDetached() {
    destroy();
  }

  private fun destroy(){
    if(countDownTimer != null){
      countDownTimer.cancel();
    }
    isCompleted = false
    TTMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback)
    container.removeAllViews();
    if(mTTSplashAd != null){
      mTTSplashAd!!.destroy();
    }
  }

  init {
    methodChannel.setMethodCallHandler(this)
    container = FrameLayout(context)
    loadAdWithCallback(params)
  }

  private fun loadAdByParam(params: Map<String, Any?>) {
    val slotId = params["slotId"] as? String
    if (slotId != null) {
      val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
      val tolerateTimeout = params["tolerateTimeout"] as? Double ?: 3
      val exceptionTimeout = tolerateTimeout.toDouble() + 1;
      hideSkipButton = params["hideSkipButton"] as? Boolean ?: false
      val imgArgs: Map<String, Int?> = params["imageSize"]?.asMap() ?: mapOf()
      val w: Int = imgArgs["width"] ?: 1080
      val h: Int = imgArgs["height"] ?: 1920

      mTTSplashAd = TTSplashAd(context, slotId)
      mTTSplashAd!!.setTTAdSplashListener(object : TTSplashAdListener {
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
      var ttNetworkRequestInfo: TTNetworkRequestInfo;
      ttNetworkRequestInfo = PangleNetworkRequestInfo("5156773", "887562558");
      //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
      mTTSplashAd!!.loadAd(adSlot, ttNetworkRequestInfo, object : TTSplashAdLoadCallback {
        override fun onSplashAdLoadFail(adError: AdError) {
          postMessage(
            "onError",
            mapOf(
              "message" to adError.message + " adMessgaeDetail=" + mTTSplashAd!!.adLoadInfoList,
              "code" to adError.code
            )
          )
        }

        override fun onSplashAdLoadSuccess() {
          if (mTTSplashAd != null) {
            mTTSplashAd!!.showAd(container)
            countDownTimer = object : CountDownTimer((exceptionTimeout *  1000).toLong(),1000){
              override fun onFinish() {
                if(!isCompleted){
                  postMessage("onTimeOver")
                }
              }
              override fun onTick(millisUntilFinished: Long) {
              }
            }
            countDownTimer.start();
          }
        }

        override fun onAdLoadTimeout() {
          postMessage("onTimeOver")
        }
      }, tolerateTimeout.toInt())
      }
  }

  private fun loadAdWithCallback(params:  Map<String, Any?>) {
    /**
     * 判断当前是否存在config 配置 ，如果存在直接加载广告 ，如果不存在则注册config加载回调
     */
    if (TTMediationAdSdk.configLoadSuccess()) {
      loadAdByParam(params)
    } else {
      TTMediationAdSdk.registerConfigCallback(mSettingConfigCallback) //不能使用内部类，否则在ondestory中无法移除该回调
    }
  }

  private val mSettingConfigCallback = TTSettingConfigCallback {
    loadAdByParam(params)
  }

  override fun getView(): View {
    return container
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    destroy();
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    when (call.method) {
      else -> result.notImplemented()
    }
  }

  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    isCompleted = true;
    if(countDownTimer != null){
      countDownTimer.cancel();
    }
    methodChannel.invokeMethod(method, arguments)
  }
}

