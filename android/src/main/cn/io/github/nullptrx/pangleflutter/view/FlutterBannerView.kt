package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.Toast
import com.bytedance.msdk.adapter.TToast
import com.bytedance.msdk.api.*
import com.bytedance.msdk.api.banner.TTAdBannerListener
import com.bytedance.msdk.api.banner.TTAdBannerLoadCallBack
import com.bytedance.msdk.api.banner.TTBannerViewAd
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.util.asMap


class FlutterBannerView(val activity: Activity, messenger: BinaryMessenger, val id: Int, params: Map<String, Any?>) : PlatformView, MethodChannel.MethodCallHandler
  {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_bannerview_$id")
  private val container: FrameLayout
  private val context: Context
  private var interval: Int? = null

  private var mTTBannerViewAd: TTBannerViewAd? = null

  init {
    methodChannel.setMethodCallHandler(this)
    loadAdWithCallback(params);
    context = activity
    container = FrameLayout(context)
  }

    private fun loadAdByParam(params: Map<String, Any?>) {
      val slotId = params["slotId"] as? String
      if (slotId != null) {
        val isAllowShowCloseBtn = params["isAllowShowCloseBtn"] as? Boolean ?: false
        val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
        interval = params["interval"] as? Int ?: 30
        val expressArgs: Map<String, Double> = params["expressSize"]?.asMap() ?: mapOf()
        val w: Float = expressArgs.getValue("width").toFloat()
        val h: Float = expressArgs.getValue("height").toFloat()

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        var adSlot: AdSlot? = AdSlot.Builder()
          .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // banner暂时只支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
          //                .setBannerSize(TTAdSize.BANNER_300_250)
          .setBannerSize(TTAdSize.BANNER_CUSTOME) // 使用TTAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
          .setImageAdSize(w.toInt(), h.toInt())
          .setSupportDeepLink(isSupportDeepLink)
          .setAdCount(1)
          .build()
        mTTBannerViewAd = TTBannerViewAd(activity, slotId)
        mTTBannerViewAd?.setRefreshTime(interval!!)  //设置banner轮播刷新事件 ,默认是开启轮播、轮播时间为30s，如果填写0，可关闭轮播功能
        mTTBannerViewAd?.setAllowShowCloseBtn(isAllowShowCloseBtn) //如果广告本身允许展示关闭按钮，这里设置为true就是展示。注：目前只有mintegral支持。
        mTTBannerViewAd?.setTTAdBannerListener(object : TTAdBannerListener {
          override fun onAdOpened() {

          }

          override fun onAdLeftApplication() {
          }

          override fun onAdClosed() {
          }

          override fun onAdClicked() {
            postMessage("onClick")
          }

          override fun onAdShow() {
          }

          /**
           * show失败回调。如果show时发现无可用广告（比如广告过期），会触发该回调。
           * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
           * @param adError showFail的具体原因
           */
          override fun onAdShowFail(adError: AdError) {
            postMessage("onError", mapOf("message" to adError.message, "code" to adError.code))
            // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载
          }
        })
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTBannerViewAd?.loadAd(adSlot, object : TTAdBannerLoadCallBack {
          override fun onAdFailedToLoad(adError: AdError) {
            container.removeAllViews()
            postMessage("onRenderFail", mapOf("message" to adError.message, "code" to adError.code))
          }

          override fun onAdLoaded() {
            container.removeAllViews()
            if (mTTBannerViewAd != null) {
              //横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
              val view = mTTBannerViewAd?.bannerView

              val params = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
              if (view != null) container.addView(view, params)
            }
            // 获取本次waterfall加载中，加载失败的adn错误信息。
          }
        })
      }
    }

    fun loadAdWithCallback(params:  Map<String, Any?>) {
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
    //注销config回调
    TTMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback)
    container.removeAllViews()
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
  }



  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    methodChannel.invokeMethod(method, arguments)
  }
}

