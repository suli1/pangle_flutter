package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.TTAdSize
import com.bytedance.msdk.api.TTDislikeCallback
import com.bytedance.msdk.api.format.TTNativeAdView
import com.bytedance.msdk.api.nativeAd.TTNativeExpressAdListener
import com.bytedance.msdk.api.nativeAd.TTVideoListener
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.PangleAdManager.Companion.shared
import io.github.nullptrx.pangleflutter.util.UIUtils

class FlutterFeedView(
  val activity: Activity,
  messenger: BinaryMessenger,
  val id: Int,
  params: Map<String, Any?>
) : PlatformView, MethodChannel.MethodCallHandler {

  private val methodChannel: MethodChannel =
    MethodChannel(messenger, "nullptrx.github.io/pangle_feedview_$id")
  private val ttNativeAdView: TTNativeAdView
  private var ttadId: String = ""
  private var containerFrameLayout: FrameLayout
  private lateinit var video: View
  private var widthParam: Double? = null

  init {
    methodChannel.setMethodCallHandler(this)
    val context: Context = activity
    //广告view的父类仍然必须是 com.bytedance.msdk.api.format.TTNativeAdView
    ttNativeAdView = TTNativeAdView(context)
    val layoutParams = FrameLayout.LayoutParams(
      FrameLayout.LayoutParams.MATCH_PARENT,
      FrameLayout.LayoutParams.MATCH_PARENT
    )
    layoutParams.gravity = Gravity.TOP
    containerFrameLayout = FrameLayout(context)
    containerFrameLayout.layoutParams = layoutParams
    ttNativeAdView.addView(containerFrameLayout)
    ttadId = params["id"] as String
    val expressArgs: Map<String, Double>? = shared.getExpressSize()
    if (expressArgs != null) {
      widthParam = expressArgs["width"]
    }
    loadAd(ttadId)
  }

  override fun getView(): View {
    return ttNativeAdView
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    containerFrameLayout.removeAllViews()
  }

  private fun loadAd(id: String) {
    val ad = shared.getExpressAdV2(id) ?: return

    //判断是否存在dislike按钮
    if (ad.hasDislike()) {
      ad.setDislikeCallback(activity, object : TTDislikeCallback {
        override fun onSelected(
          position: Int,
          value: String
        ) {
          postMessage("onDislike", mapOf("option" to value))
        }

        override fun onCancel() {
        }

        /**
         * 拒绝再次提交
         */
        override fun onRefuse() {
        }

        override fun onShow() {
        }
      })
    }

    //设置点击展示回调监听

    //设置点击展示回调监听
    ad.setTTNativeAdListener(object : TTNativeExpressAdListener {
      override fun onAdClick() {
      }

      override fun onAdShow() {
      }

      override fun onRenderFail(
        view: View,
        msg: String,
        code: Int
      ) {
        postMessage("onError", mapOf("message" to msg, "code" to code))
      }

      // ** 注意点 ** 不要在广告加载成功回调里进行广告view展示，要在onRenderSucces进行广告view展示，否则会导致广告无法展示。
      override fun onRenderSuccess(
        width: Float,
        height: Float
      ) {
        //回调渲染成功后将模板布局添加的父View中
        //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
        val sWidth: Int
        val sHeight: Int
        /**
         * 如果存在父布局，需要先从父布局中移除
         */
        video = ad.expressView // 获取广告view  如果存在父布局，需要先从父布局中移除
        if (width == TTAdSize.FULL_WIDTH.toFloat() && height == TTAdSize.AUTO_HEIGHT.toFloat()) {
          sWidth = FrameLayout.LayoutParams.MATCH_PARENT
          sHeight = WRAP_CONTENT
        } else {
          sWidth = UIUtils.getScreenWidth(activity)
          sHeight = (sWidth * height / width).toInt()
        }
        if (video != null) {
          /**
           * 如果存在父布局，需要先从父布局中移除
           */
          UIUtils.removeFromParent(video)
          val layoutParams = FrameLayout.LayoutParams(sWidth, sHeight)
          containerFrameLayout.removeAllViews()
          containerFrameLayout.addView(video, layoutParams)
          video.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
              val height: Double
              if (widthParam != null) {
                height = video.measuredHeight * widthParam!! / video.measuredWidth
              } else {
                height = video.measuredHeight.toDouble()
              }
              postMessage(
                "onRenderSuccess",
                mapOf("width" to video.measuredWidth.toDouble(), "height" to height)
              )
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ttNativeAdView.viewTreeObserver.removeOnGlobalLayoutListener(this)
              } else {
                ttNativeAdView.viewTreeObserver.removeGlobalOnLayoutListener(this)
              }
            }
          })
        }
      }
    })

    //视频广告设置播放状态回调（可选）

    //视频广告设置播放状态回调（可选）
    ad.setTTVideoListener(object : TTVideoListener {
      override fun onVideoStart() {
      }

      override fun onVideoPause() {
      }

      override fun onVideoResume() {
      }

      override fun onVideoCompleted() {
      }

      override fun onVideoError(adError: AdError) {
      }
    })

    ad.render()
  }

  override fun onMethodCall(
    call: MethodCall,
    result: MethodChannel.Result
  ) {
  }

  private fun postMessage(
    method: String,
    arguments: Map<String, Any?> = mapOf()
  ) {
    methodChannel.invokeMethod(method, arguments)
  }
}
