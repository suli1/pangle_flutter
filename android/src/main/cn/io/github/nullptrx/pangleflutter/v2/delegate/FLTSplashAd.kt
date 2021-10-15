package io.github.nullptrx.pangleflutter.v2.delegate

import android.app.Activity
import android.widget.FrameLayout
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.splash.TTSplashAd
import com.bytedance.msdk.api.splash.TTSplashAdLoadCallback
import io.github.nullptrx.pangleflutter.common.kBlock
import io.github.nullptrx.pangleflutter.dialog.NativeSplashDialog
import io.github.nullptrx.pangleflutter.dialog.SupportSplashDialog

internal class FLTSplashAdV2(
    val ttSplashAd: TTSplashAd,
    val hideSkipButton: Boolean?,
    var activity: Activity?,
    var result: (Any) -> Unit = {}
) : TTSplashAdLoadCallback {
    private var supportDialog: SupportSplashDialog? = null
    private var nativeDialog: NativeSplashDialog? = null


    private fun handleSplashEnd() {
        supportDialog?.dismissAllowingStateLoss()
        nativeDialog?.dismissAllowingStateLoss()
    }


    fun invoke(code: Int = 0, message: String = "") {
        if (result == kBlock) {
            return
        }
        result.apply {
            val params = mutableMapOf<String, Any>()
            params["code"] = code
            params["message"] = message
            invoke(params)
            result = kBlock
        }
    }

    override fun onSplashAdLoadFail(p0: AdError?) {
        handleSplashEnd()
        invoke(p0!!.code, p0.message)
    }

    override fun onSplashAdLoadSuccess() {
        val container = FrameLayout(activity!!)
        ttSplashAd.showAd(container)
    }

    override fun onAdLoadTimeout() {
        handleSplashEnd()
        invoke(-1, "timeout")
    }


}
