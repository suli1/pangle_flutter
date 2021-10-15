package io.github.nullptrx.pangleflutter.delegate

import android.app.Activity
import io.github.nullptrx.pangleflutter.common.PangleLoadingType

internal class FLTRewardedVideoAd(
  val slotId: String,
  var target: Activity?,
  val loadingType: PangleLoadingType,
  var result: (Any) -> Unit = {}
) {

  private fun invoke(
    code: Int = 0,
    message: String? = null,
    verify: Boolean = false
  ) {
    result.apply {
      val args = mutableMapOf<String, Any?>()
      args["code"] = code
      message?.also {
        args["message"] = it
      }
      if (code == 0) {
        args["verify"] = verify
      }
      invoke(args)
    }
    result = {}
    target = null
  }
}

