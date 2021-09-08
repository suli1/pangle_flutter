package io.github.nullptrx.pangleflutter.v2.delegate

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.splash.TTSplashAd
import com.bytedance.msdk.api.splash.TTSplashAdLoadCallback
import com.bytedance.sdk.openadsdk.TTAdNative
import io.github.nullptrx.pangleflutter.common.kBlock
import io.github.nullptrx.pangleflutter.dialog.NativeSplashDialog
import io.github.nullptrx.pangleflutter.dialog.SupportSplashDialog

internal class FLTSplashAdV2(
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
        Toast.makeText(activity, "失败", Toast.LENGTH_LONG).show();
        handleSplashEnd()
        invoke(p0!!.code, p0.message)
    }

    override fun onSplashAdLoadSuccess() {
    }

    override fun onAdLoadTimeout() {
        Toast.makeText(activity, "超时", Toast.LENGTH_LONG).show();
        handleSplashEnd()
        invoke(-1, "timeout")
    }


}