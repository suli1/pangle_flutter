//
//  ABUNativeAd.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/26.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUAdUnit.h"
#import "ABUMaterialMeta.h"
#import "ABUNativeAdDelegate.h"
#import "ABUAdapterInfoModel.h"
#import "ABUAdSDKDefines.h"

@protocol ABUAdNetworkAdapterProtocol;

@interface ABUNativeAdView : UIView

@property (nonatomic, strong) ABUNativeAdsManager *_Nullable adManager;     // 隶属的广告管理类
@property (nonatomic, strong) ABUAdUnit *_Nullable originSlot;     // 广告单元信息，包含一些必要属性

/**
 The delegate for receiving state change messages.
 The delegate is not limited to viewcontroller.
 The delegate can be set to any object which conforming to <ABUNativeAdDelegate>.
 */
@property (nonatomic, weak, readwrite, nullable) id<ABUNativeAdViewDelegate> delegate;

/**
 The delegate is for native video Ads.
 The delegate can be set to video Ads  object which conforming to <ABUNativeAdViewDelegate>.
 */
@property (nonatomic, weak, readwrite, nullable) id<ABUNativeAdVideoDelegate> videoDelegate;

/**
 Extended property, now is unavailable. Indicates whether video should start play auto. By default this property value is NO. It is only valid when the third adn is supported and current ad type is native video(data.imageMode == ABUFeedVideoAdModeImage).
 扩展字段，暂不可用！
 信息流视频是否自动播放,默认为NO。仅在第三方Adn支持时可生效(若设置未生效说明三方不支持并不是聚合问题).同时，使用时要判断当前广告类型data.imageMode == ABUFeedVideoAdModeImage
 */
@property (nonatomic, assign, readonly) BOOL autoPlayIfcan;

/**
 Extended property, now is unavailable.
 扩展字段，暂不可用！
 control the video playbackcontrol the video playback.
 是否可以控制视频播放
 Generally if it is NO, it will be YES when third Adn is supported.
 当三方Adn支持时，开发者可手动控制视频播放.
 The valid premise of the value is when data.imageMode == ABUFeedVideoAdModeImage.
 有效前提是data.imageMode == ABUFeedVideoAdModeImage.
 */
@property (nonatomic, assign, readonly) BOOL canConfigVideoPlay;

/**
 The following four functions about video are only valid when the property "canConfigVideoPlay" is YES.If you want to use those functions, you should make sure that autoPlayIfcan is NO.
 以下四项有关视频的方法只有在canConfigVideoPlay=YES时，有效。使用时，需将autoPlayIfcan设置为NO.
 */
- (BOOL)isPlaying;
- (void)play;
- (void)pause;
- (void)stop;

/**
 required.
 Root view controller for handling ad actions.
 Action method includes 'pushViewController' and 'presentViewController'.
 */
@property (nonatomic, weak, readwrite) UIViewController *_Nullable rootViewController;
/**
 Initializes native ad with ad slot.
 @param adUnit : ad unit description.
               including slotID,adType,adPosition,etc.
 @return ABUNativeAd
 */
- (instancetype _Nonnull)initWithUnit:(ABUAdUnit *_Nonnull)adUnit;

/*************************************************自渲染接口 Start********************************************************************/

/**
 Ad material.It use to render ads by developer themselves when isExpressAd is NO.
 广告物料，当isExpressAd=NO时用于开发者自行渲染广告
 */
@property (nonatomic, strong, readonly, nullable) ABUMaterialMeta *data;

/**
 Use for Ad which is not expressed(isExpressAd=NO).
 Register clickable views in native ads view.
 Interaction types can be configured on TikTok Audience Network.
 Interaction types include view video ad details page, make a call, send email, download the app, open the webpage using a browser,open the webpage within the app, etc.
 @param clickableViews : optional.
                        Array of views that are clickable.
 */
- (void)registerClickableViews:(NSArray<__kindof UIView *> *_Nullable)clickableViews;

// 若返回为自渲染广告，开发者需自己渲染布局返回的View，需自渲染以下内容

/*************************************************自渲染接口 End********************************************************************/

/*************************************************自渲染视图 Start********************************************************************/

/// hasSupportActionBtn. Whether to support adding button:callToActionBtn.If hasSupportActionBtn is YES, you can setuo frame of callToActionBtn
@property (nonatomic, assign) BOOL hasSupportActionBtn;

/// Ad Title. Need to be assigned from a data(ABUMaterialMeta).
@property (nonatomic, strong) UILabel *_Nullable titleLabel;

/// Ad Desc. Need to be assigned from a data(ABUMaterialMeta).
@property (nonatomic, strong) UILabel *_Nullable descLabel;

/// Ad Icon.Need to judge whether the value is empty.If If customized, it needs to be added to self(ABUNativeAdView)
@property (nonatomic, strong) UIImageView *_Nullable iconImageView;

/// Ad image. Need to be assigned from a data(ABUMaterialMeta), and need to be add to self(ABUNativeAdView).
@property (nonatomic, strong) UIImageView *_Nullable imageView;

/// Ad CTA button. Need to be assigned from a data(ABUMaterialMeta), and need to be add to self(ABUNativeAdView).
@property (nonatomic, strong) UIButton *_Nullable callToActionBtn;

/// Ad logo. Need to judge whether the value is empty.If If customized, it needs to be added to self(ABUNativeAdView).
@property (nonatomic, strong, nullable) UIView *adLogoView ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 ABUMaterialMeta里的adLogo自行渲染");

/// dislikeBtn.Need to judge whether the value is empty.If If customized, it needs to be added to self(ABUNativeAdView)
@property (nonatomic, strong, nullable) UIView *advertiserView;

/// dislikeBtn.Need to judge whether the value is empty.If customized, it needs to be added to self(ABUNativeAdView)
@property (nonatomic, strong, nullable) UIButton *dislikeBtn;

/**
 Video ad view. Need to be assigned from a data(ABUMaterialMeta).
 */
@property (nonatomic, strong, nullable) UIView *mediaView;

/*************************************************自渲染视图 End********************************************************************/

/*************************************************模板接口 Start********************************************************************/

// 若返回为模板广告，返回View可直接使用
/**
 Is a express Ad
 是否为模板广告，isExpressAd=YES时可调用render渲染广告;在外部设置getExpressAdIfCanw=YES时如果第三方支持会返回模板广告
 */
@property (nonatomic, assign, readonly) BOOL hasExpressAdGot;

/**
 required if hasExpressAdGot=YES
 */
- (void)render;

/**
required if frame of mediaView is resetup.
*/
- (void)reSizeMediaView;
/*************************************************模板接口 End********************************************************************/

/// 返回显示广告对应的Adn，当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3
- (ABUAdnType)getAdNetworkPlaformId;
/// 返回显示广告对应的rit，当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3
- (NSString *_Nullable)getAdNetworkRitId;
/// 返回显示广告对应的ecpm，当未在平台配置ecpm会返回-1，当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3 单位：分
- (NSString *_Nullable)getPreEcpm;

@end
