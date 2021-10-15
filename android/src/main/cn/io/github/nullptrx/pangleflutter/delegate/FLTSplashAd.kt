package io.github.nullptrx.pangleflutter.delegate

import android.app.Activity
import io.github.nullptrx.pangleflutter.common.kBlock
import io.github.nullptrx.pangleflutter.dialog.NativeSplashDialog
import io.github.nullptrx.pangleflutter.dialog.SupportSplashDialog

internal class FLTSplashAd(
    val hideSkipButton: Boolean?,
    var activity: Activity?,
    var result: (Any) -> Unit = {}
) {
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


}
