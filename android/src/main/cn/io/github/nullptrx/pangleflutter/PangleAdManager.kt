package io.github.nullptrx.pangleflutter

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import com.bytedance.msdk.api.nativeAd.TTNativeAd
import io.github.nullptrx.pangleflutter.common.PangleLoadingType
import io.github.nullptrx.pangleflutter.common.PangleTitleBarTheme
import io.github.nullptrx.pangleflutter.common.TTSizeF
import io.github.nullptrx.pangleflutter.delegate.*
import io.github.nullptrx.pangleflutter.util.asList
import io.github.nullptrx.pangleflutter.util.asMap
import java.util.*


class PangleAdManager {

  companion object {
    val shared = PangleAdManager()
  }

  private val expressAdCollec1tionV2 =
    Collections.synchronizedMap(mutableMapOf<String, TTNativeAd>())


    get() = field





  /**
   * Express
   */
  fun setExpressAdV2(ttBannerAds: List<TTNativeAd>): List<String> {
    val data = mutableListOf<String>()
    ttBannerAds.forEach {
      val key = it.hashCode().toString()
      expressAdCollec1tionV2[key] = it
      data.add(key)
    }
    return data
  }

  fun getExpressAdV2(key: String): TTNativeAd? {
    return expressAdCollec1tionV2[key]
  }



  fun removeExpressAdV2(key: String): Boolean {
    if (expressAdCollec1tionV2.containsKey(key)) {
      val it = expressAdCollec1tionV2.remove(key)
      it?.destroy()
      return true
    }
    return false
  }


}

