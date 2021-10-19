package io.github.nullptrx.pangleflutter.util;

import io.flutter.plugin.common.MethodChannel;
import io.github.nullptrx.pangleflutter.bean.PangleResult;

public class MessageUtils {

  public static void postSimpleMessage(MethodChannel.Result result, int code, String message) {
    PangleResult pangleResult = new PangleResult();
    pangleResult.code = code;
    pangleResult.message = message;
    postCustomMessage(result, pangleResult);
  }

  public static void postVerifyMessage(MethodChannel.Result result, int code, String message,
      Boolean isVerify) {
    PangleResult pangleResult = new PangleResult();
    pangleResult.code = code;
    pangleResult.message = message;
    pangleResult.verify = isVerify;
    postCustomMessage(result, pangleResult);
  }

  public static void postCustomMessage(MethodChannel.Result result, PangleResult pangleResult) {
    if (result != null && pangleResult != null) {
      try {
        result.success(pangleResult.toMap());
      } catch (Exception ignored) {
      }
    }
  }
}
