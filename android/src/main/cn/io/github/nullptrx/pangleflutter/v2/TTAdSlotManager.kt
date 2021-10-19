package io.github.nullptrx.pangleflutter.v2

import android.text.TextUtils
import com.bytedance.msdk.api.AdSlot
import com.bytedance.msdk.api.AdSlot.Builder
import com.bytedance.msdk.api.TTAdConstant
import com.bytedance.msdk.api.TTVideoOption
import io.flutter.plugin.common.MethodCall
import io.github.nullptrx.pangleflutter.PangleAdManager.Companion.shared
import io.github.nullptrx.pangleflutter.util.VideoOptionUtil.splashProLoadTTVideoOption
import io.github.nullptrx.pangleflutter.util.VideoOptionUtil.tTVideoOption
import io.github.nullptrx.pangleflutter.util.VideoOptionUtil.tTVideoOption2

object TTAdSlotManager {
  fun getSplashAdSlot(
    width: Int,
    height: Int,
    supportDeepLink: Boolean
  ): AdSlot {
    val videoOption = splashProLoadTTVideoOption
    return Builder()
      .setTTVideoOption(videoOption)
      .setImageAdSize(width, height) // 既适用于原生类型，也适用于模版类型。
      .setSupportDeepLink(supportDeepLink) //NEW ADD
      .setSplashButtonType(TTAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN)
      .setDownloadType(TTAdConstant.DOWNLOAD_TYPE_POPUP)
      .build()
  }

  fun getInterstitialAdAdSlot(
    width: Float?,
    height: Float?,
    supportDeepLink: Boolean
  ): AdSlot {
    val videoOption = tTVideoOption
    return Builder()
      .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 注意：插屏暂时支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
      .setTTVideoOption(videoOption)
      .setSupportDeepLink(supportDeepLink)
      .setImageAdSize(
        width?.toInt() ?: 0, height?.toInt() ?: 0
      ) //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
      .build()
  }

  fun getRewardAdSlot(call: MethodCall): AdSlot {
    val videoOption = TTVideoOption.Builder()
      .setMuted(true) //对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
      .setAdmobAppVolume(0f) //配合Admob的声音大小设置[0-1]
      .build()
    val builder =
      Builder().setTTVideoOption(videoOption)
    val userId = call.argument<String>("userId")
    if (!TextUtils.isEmpty(userId)) {
      builder.setUserID(userId)
    }
    val rewardName = call.argument<String>("rewardName")
    if (!TextUtils.isEmpty(rewardName)) {
      builder.setRewardName(rewardName)
    }
    val rewardAmount = call.argument<Int>("rewardAmount")
    if (!TextUtils.isEmpty(rewardName)) {
      builder.setRewardAmount(rewardAmount ?: 0)
    }
    val extra = call.argument<String>("extra")
    val isVertical = call.argument<Boolean>("isVertical")
    builder.setOrientation(
      if (isVertical == null || isVertical) TTAdConstant.VERTICAL else TTAdConstant.HORIZONTAL
    )
    val isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink")
    val expressArgs =
      call.argument<Map<String, Double>>("expressSize")
    if (expressArgs != null) {
      var w = expressArgs["width"]
      if (w == null) {
        w = 0.00
      }
      var h = expressArgs["height"]
      if (h == null) {
        h = 0.00
      }
      builder.setImageAdSize(w.toInt(), h.toInt())
    }
    return builder
      .setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 确保该聚合广告位下所有gdt代码位都是同一种类型（模版或非模版）。
      .setSupportDeepLink(isSupportDeepLink!!)
      .setMediaExtra(extra)
      .build()
  }

  fun getFeedListAdSlot(call: MethodCall): AdSlot {
    val videoOption = tTVideoOption2
    val builder =
      Builder().setTTVideoOption(videoOption)
    var isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink")
    if (isSupportDeepLink == null) {
      isSupportDeepLink = false
    }
    val expressArgs =
      call.argument<Map<String, Double>>("expressSize")
    if (expressArgs != null) {
      var w = expressArgs["width"]
      if (w == null) {
        w = 0.00
      }
      var h = expressArgs["height"]
      if (h == null) {
        h = 0.00
      }
      builder.setImageAdSize(w.toInt(), h.toInt()) // 必选参数 单位dp ，详情见上面备注解释
    }
    shared.setExpressSize(expressArgs)
    val count = call.argument<Int>("count")

    return builder
      .setAdStyleType(
        AdSlot.TYPE_EXPRESS_AD
      )
      //必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告
      // 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
      // 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
      .setAdCount(count ?: 0) //请求广告数量为1到3条
      .setSupportDeepLink(isSupportDeepLink) //.setGdtNativeAdLogoParams(gdtNativeAdLogoParams)
      .build()
  }

  fun getFullVideoAdSlot(call: MethodCall): AdSlot {
    val videoOption = tTVideoOption
    val builder =
      Builder().setTTVideoOption(videoOption)
    val orientationIndex = call.argument<Int>("orientation")
    if (orientationIndex == null) {
      builder.setOrientation(TTAdConstant.VERTICAL)
    } else {
      if (orientationIndex == 0 || orientationIndex == 1) {
        builder.setOrientation(TTAdConstant.VERTICAL)
      } else {
        builder.setOrientation(TTAdConstant.HORIZONTAL)
      }
    }
    val userId = call.argument<String>("userId")
    if (!TextUtils.isEmpty(userId)) {
      builder.setUserID(userId)
    }
    var isSupportDeepLink = call.argument<Boolean>("isSupportDeepLink")
    if (isSupportDeepLink == null) {
      isSupportDeepLink = false
    }
    val expressArgs =
      call.argument<Map<String, Double>>("expressSize")
    if (expressArgs != null) {
      var w = expressArgs["width"]
      if (w == null) {
        w = 0.00
      }
      var h = expressArgs["height"]
      if (h == null) {
        h = 0.00
      }
      builder.setImageAdSize(w.toInt(), h.toInt())
    }
    return builder
      .setSupportDeepLink(isSupportDeepLink)
      .build()
  }
}
