package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.AdSlot
import com.bytedance.msdk.api.TTAdSize
import com.bytedance.msdk.api.banner.TTAdBannerListener
import com.bytedance.msdk.api.banner.TTAdBannerLoadCallBack
import com.bytedance.msdk.api.banner.TTBannerViewAd
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.common.TTSizeF
import io.github.nullptrx.pangleflutter.util.asMap

class FlutterBannerView(val activity: Activity, messenger: BinaryMessenger, val id: Int, params: Map<String, Any?>) : PlatformView, MethodChannel.MethodCallHandler, TTAdNative.NativeExpressAdListener,
    TTNativeExpressAd.AdInteractionListener, TTAdDislike.DislikeInteractionCallback {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_bannerview_$id")
  private val container: FrameLayout
  private val context: Context
  private var interval: Int? = null
  private var ttAdNative: TTNativeExpressAd? = null

  private var mTTBannerViewAd: TTBannerViewAd? = null

  init {
    methodChannel.setMethodCallHandler(this)
    context = activity
    container = FrameLayout(context)

    val slotId = params["slotId"] as? String
    if (slotId != null) {

      val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
      interval = params["interval"] as Int?


      val expressArgs: Map<String, Double> = params["expressSize"]?.asMap() ?: mapOf()
      val w: Float = expressArgs.getValue("width").toFloat()
      val h: Float = expressArgs.getValue("height").toFloat()
      val expressSize = TTSizeF(w, h)

      /**
       * 注：每次加载banner的时候需要新建一个TTBannerViewAd，否则可能会出现广告填充问题
       * （ 例如：mTTBannerViewAd = new TTBannerViewAd(this, adUnitId);）
       */

      //step4:创建广告请求参数AdSlot,具体参数含义参考文档
      var adSlot: AdSlot? = AdSlot.Builder()
              .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // banner暂时只支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
              //                .setBannerSize(TTAdSize.BANNER_300_250)
              .setBannerSize(TTAdSize.BANNER_CUSTOME) // 使用TTAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
              .setImageAdSize(320, 150)
              .build()

      mTTBannerViewAd = TTBannerViewAd(activity,slotId)
      mTTBannerViewAd?.setRefreshTime(30)
      mTTBannerViewAd?.setAllowShowCloseBtn(true) //如果广告本身允许展示关闭按钮，这里设置为true就是展示。注：目前只有mintegral支持。

      mTTBannerViewAd?.setTTAdBannerListener(object : TTAdBannerListener {
        override fun onAdOpened() {

        }

        override fun onAdLeftApplication() {
        }

        override fun onAdClosed() {
        }

        override fun onAdClicked() {
        }

        override fun onAdShow() {
        }

        /**
         * show失败回调。如果show时发现无可用广告（比如广告过期），会触发该回调。
         * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
         * @param adError showFail的具体原因
         */
        override fun onAdShowFail(adError: AdError) {

          // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载
        }
      })
      //step5:请求广告，对请求回调的广告作渲染处理
      mTTBannerViewAd?.loadAd(adSlot, object : TTAdBannerLoadCallBack {
        override fun onAdFailedToLoad(adError: AdError) {
          container.removeAllViews()
          // 获取本次waterfall加载中，加载失败的adn错误信息。
        }

        override fun onAdLoaded() {
          container.removeAllViews()
          if (mTTBannerViewAd != null) {
            //横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
            val view = mTTBannerViewAd?.bannerView

            val params = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            if (view != null) container.addView(view,params)
          }
          // 获取本次waterfall加载中，加载失败的adn错误信息。
        }
      })
    }
  }

  override fun getView(): View {
    return container
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    ttAdNative?.destroy()
    container.removeAllViews()
  }

  override fun onError(code: Int, message: String?) {
    postMessage("onError", mapOf("message" to message, "code" to code))
  }

  override fun onNativeExpressAdLoad(ttNativeExpressAds: MutableList<TTNativeExpressAd>?) {
    if (ttNativeExpressAds == null || ttNativeExpressAds.isEmpty()) {
      return
    }

    val ad = ttNativeExpressAds[0]
    ttAdNative = ad
    //设置广告互动监听回调
    ad.setExpressInteractionListener(this)

    //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
    ad.setDislikeCallback(activity, this)
    // 设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
    interval?.also {
      ad.setSlideIntervalTime(it)
    }

    container.removeAllViews()
    val params = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    val expressAdView = ad.expressAdView
    container.addView(expressAdView, params)
//      container.addView(expressAdView)
    ad.render()
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
  }


  override fun onAdDismiss() {
  }

  override fun onAdClicked(view: View, type: Int) {
    postMessage("onClick")
  }

  override fun onAdShow(view: View, type: Int) {
    postMessage("onShow")
  }

  override fun onRenderSuccess(view: View, width: Float, height: Float) {
    postMessage("onRenderSuccess")
  }

  override fun onRenderFail(view: View, message: String?, code: Int) {
    postMessage("onRenderFail", mapOf("message" to message, "code" to code))
  }

  override fun onShow() {

  }

  override fun onSelected(index: Int, option: String?, enforce: Boolean) {
    //用户选择不喜欢原因后，移除广告展示
    postMessage("onDislike", mapOf("option" to option, "enforce" to enforce))
  }

  override fun onCancel() {
  }

  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    methodChannel.invokeMethod(method, arguments)
  }
}

