package io.github.nullptrx.pangleflutter

import com.bytedance.msdk.api.nativeAd.TTNativeAd
import java.util.Collections

class PangleAdManager {

  companion object {
    val shared = PangleAdManager()
  }

  private val expressAdCollectionV2 =
    Collections.synchronizedMap(mutableMapOf<String, TTNativeAd>())

  private var expressSize: Map<String, Double>? = null

  fun setExpressSize(map: Map<String, Double>?) {
    expressSize = map
  }

  fun getExpressSize(): Map<String, Double>? {
    return expressSize
  }

  /**
   * Express
   */
  fun setExpressAdV2(ttBannerAds: List<TTNativeAd>): List<String> {
    val data = mutableListOf<String>()
    ttBannerAds.forEach {
      val key = it.hashCode().toString()
      expressAdCollectionV2[key] = it
      data.add(key)
    }
    return data
  }

  fun getExpressAdV2(key: String): TTNativeAd? {
    return expressAdCollectionV2[key]
  }

  fun removeExpressAdV2(key: String): Boolean {
    if (expressAdCollectionV2.containsKey(key)) {
      val it = expressAdCollectionV2.remove(key)
      it?.destroy()
      return true
    }
    return false
  }
}

