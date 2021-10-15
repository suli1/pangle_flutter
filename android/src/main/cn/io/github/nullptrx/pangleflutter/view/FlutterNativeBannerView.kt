package io.github.nullptrx.pangleflutter.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import io.github.nullptrx.pangleflutter.common.TTSize
import io.github.nullptrx.pangleflutter.util.asMap

class FlutterNativeBannerView(
    val context: Context,
    messenger: BinaryMessenger,
    val id: Int,
    params: Map<String, Any?>
) : PlatformView, MethodChannel.MethodCallHandler {

    private val methodChannel: MethodChannel =
        MethodChannel(messenger, "nullptrx.github.io/pangle_nativebannerview_$id")
    private val container: FrameLayout
    private var interval: Int? = null


    init {
        methodChannel.setMethodCallHandler(this)
        container = FrameLayout(context)

        val slotId = params["slotId"] as? String
        if (slotId != null) {

            val isSupportDeepLink = params["isSupportDeepLink"] as? Boolean ?: true
            interval = params["interval"] as Int?


            val expressArgs: Map<String, Double> = params["size"]?.asMap() ?: mapOf()
            val w: Int = expressArgs.getValue("width").toInt()
            val h: Int = expressArgs.getValue("height").toInt()
            val size = TTSize(w, h)
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
    }

    private fun postMessage(method: String, arguments: Map<String, Any?> = mapOf()) {
        methodChannel.invokeMethod(method, arguments)
    }
}

