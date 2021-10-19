package io.github.nullptrx.pangleflutter.util

import io.flutter.plugin.common.MethodChannel.Result
import io.github.nullptrx.pangleflutter.bean.PangleResult

object MessageUtils {
  fun postSimpleMessage(
    result: Result?,
    code: Int,
    message: String?
  ) {
    val pangleResult = PangleResult()
    pangleResult.code = code
    pangleResult.message = message
    postCustomMessage(result, pangleResult)
  }

  fun postVerifyMessage(
    result: Result?,
    code: Int,
    message: String?,
    isVerify: Boolean?
  ) {
    val pangleResult = PangleResult()
    pangleResult.code = code
    pangleResult.message = message
    pangleResult.verify = isVerify
    postCustomMessage(result, pangleResult)
  }

  fun postCustomMessage(
    result: Result?,
    pangleResult: PangleResult?
  ) {
    if (result != null && pangleResult != null) {
      try {
        result.success(pangleResult.toMap())
      } catch (e: Exception) {
//        Log.e("pangle", "post message failed:${pangleResult.toMap()}, $e")
      }
    }
  }
}
