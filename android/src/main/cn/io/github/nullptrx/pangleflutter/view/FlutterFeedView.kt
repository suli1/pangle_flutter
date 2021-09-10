package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
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
import io.github.nullptrx.pangleflutter.PangleAdManager
import io.github.nullptrx.pangleflutter.util.UIUtils
import io.github.nullptrx.pangleflutter.util.UIUtils.removeFromParent


class FlutterFeedView(
    val activity: Activity,
    messenger: BinaryMessenger,
    val id: Int,
    params: Map<String, Any?>
) : PlatformView, MethodChannel.MethodCallHandler {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_feedview_$id")
  private val container: TTNativeAdView
  private var ttadId: String = ""

  init {
    methodChannel.setMethodCallHandler(this)
    val context: Context = activity
    //广告view的父类仍然必须是 com.bytedance.msdk.api.format.TTNativeAdView
    container = TTNativeAdView(context);
    ttadId = params["id"] as String
    loadAd(ttadId)
  }

  override fun getView(): View {
    return container
  }

  override fun dispose() {
    methodChannel.setMethodCallHandler(null)
    container.removeAllViews()
  }

  private fun loadAd(id: String) {
    val ad = PangleAdManager.shared.getExpressAdV2(id) ?: return


    //判断是否存在dislike按钮
    if (ad.hasDislike()) {
      ad.setDislikeCallback(activity, object : TTDislikeCallback {
        override fun onSelected(position: Int, value: String) {
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

      override fun onRenderFail(view: View, msg: String, code: Int) {
        postMessage("onError", mapOf("message" to msg, "code" to code))
      }

      // ** 注意点 ** 不要在广告加载成功回调里进行广告view展示，要在onRenderSucces进行广告view展示，否则会导致广告无法展示。
      override fun onRenderSuccess(width: Float, height: Float) {
        //回调渲染成功后将模板布局添加的父View中
        if (container != null) {
          //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
          val sWidth: Int
          val sHeight: Int

          /**
           * 如果存在父布局，需要先从父布局中移除
           */
          val video: View = ad.expressView // 获取广告view  如果存在父布局，需要先从父布局中移除
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
            container.removeAllViews()
            container.addView(video, layoutParams)
          }

//          val expressAdView = ad.expressView
//          if (expressAdView.parent != null) {
//            (expressAdView.parent as ViewGroup).removeView(expressAdView)
//          }
//          container.removeAllViews()
//          val params = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
//            gravity = Gravity.CENTER
//          }
//          container.addView(expressAdView, params)
          postMessage("onRenderSuccess")
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

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
  }


  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    methodChannel.invokeMethod(method, arguments)
  }
}