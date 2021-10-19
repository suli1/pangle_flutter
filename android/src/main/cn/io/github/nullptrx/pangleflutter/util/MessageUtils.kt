package io.github.nullptrx.pangleflutter.util

import io.flutter.plugin.common.MethodChannel.Result
import io.github.nullptrx.pangleflutter.bean.PangleResult

object MessageUtils {
  @JvmStatic fun postSimpleMessage(
    result: Result?,
    code: Int,
    message: String?
  ) {
    val pangleResult = PangleResult()
    pangleResult.code = code
    pangleResult.message = message
    postCustomMessage(result, pangleResult)
  }

  @JvmStatic fun postVerifyMessage(
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

  @JvmStatic fun postCustomMessage(
    result: Result?,
    pangleResult: PangleResult?
  ) {
    if (result != null && pangleResult != null) {
      try {
        result.success(pangleResult.toMap())
      } catch (ignored: Exception) {
      }
    }
  }
}
