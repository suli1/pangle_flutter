package io.github.nullptrx.pangleflutter.view

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.bytedance.msdk.api.TTDislikeCallback
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.PangleAdManager


class FlutterFeedView(
    val activity: Activity,
    messenger: BinaryMessenger,
    val id: Int,
    params: Map<String, Any?>
) : PlatformView, MethodChannel.MethodCallHandler, TTDislikeCallback {

  private val methodChannel: MethodChannel = MethodChannel(messenger, "nullptrx.github.io/pangle_feedview_$id")
  private val container: FrameLayout
  private var ttadId: String = ""

  init {
    methodChannel.setMethodCallHandler(this)
    val context: Context = activity
    container = FrameLayout(context)
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
    val expressAd = PangleAdManager.shared.getExpressAdV2(id) ?: return
    val expressAdView = expressAd.expressView
    if (expressAdView.parent != null) {
      (expressAdView.parent as ViewGroup).removeView(expressAdView)
    }
    container.removeAllViews()

    val params = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
      gravity = Gravity.CENTER
    }
    container.addView(expressAdView, params)
//    expressAd.setCanInterruptVideoPlay(true)
//    expressAd.setExpressInteractionListener(this)
    expressAd.setDislikeCallback(activity, this)
    expressAd.render()

  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
  }


  override fun onShow() {
    postMessage("onShow")
  }


  override fun onSelected(p0: Int, p1: String?) {
    postMessage("onDislike", mapOf("option" to Int, "enforce" to p1))
  }

  override fun onCancel() {

  }

  override fun onRefuse() {
    postMessage("onRenderSuccess")
  }

  private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
    methodChannel.invokeMethod(method, arguments)
  }
}