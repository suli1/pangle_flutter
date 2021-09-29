//
//  ABUBannerAd.h
//  ABUAdSDK
//
//  Created by XuQingJia on 2020/11/22.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUBaseAggregationAd.h"
#import "ABUBannerAdDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface ABUBannerAd : ABUBaseAggregationAd

@property (nonatomic, weak, nullable) id<ABUBannerAdDelegate> delegate;

/**
 The carousel interval, in seconds, is set in the range of 30~120s, and is passed during initialization. If it does not meet the requirements, it will not be in carousel ad.
 !!!Some Ad SDK is not support setup by Client, eg:AdMob & Baidu & UnityAds.So it is valid of theseSDK.
  supported.
 */
@property (nonatomic, assign, readonly) NSInteger autoRefreshTime;

/**
 required.
 Root view controller for handling ad actions.
 Action method includes 'pushViewController' and 'presentViewController'.
 */
@property (nonatomic, weak, readwrite) UIViewController *_Nullable rootViewController;

/// adSize
@property (nonatomic, assign, readonly) CGSize adSize;

/**
 Is a express Ad
 返回是否为模板广告，一般如果有返回值在收到visiable方法可用
 Generally if there is a return value available in the receive method "AdDidVisible"
 */
@property (nonatomic, assign, readonly) BOOL hasExpressAdGot;

/// Configure whether the request is successful
@property (nonatomic, assign, readonly) BOOL hasAdConfig ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 '+ [ABUAdSDKManager configDidLoad]' 方法代替");

/// Description
/// @param adUnitID adUnitID
/// @param rootViewController rootViewController
/// @param adsize adsize The adsize should be equal to the ad size configured by the platform.Or setup 0 for Adaptive height。
/// @param autoRefreshTime The carousel interval, in seconds, is set in the range of 30~120s, and is passed during initialization. If it does not meet the requirements, it will not be in carousel ad.There is no effect when ads are not supported
/// 广告刷新间隔，范围 [30, 120] 秒，默认值 30 秒。设 0 则不刷新。当广告不支持时设置无用
- (instancetype _Nonnull)initWithAdUnitID:(NSString *_Nonnull)adUnitID
                       rootViewController:(UIViewController *_Nonnull)rootViewController
                                   adSize:(CGSize)adsize autoRefreshTime:(NSInteger)autoRefreshTime;

/// load Ad
- (void)loadAdData;

/// The method is called if the hasAdConfig is NO.
/// @param callback  loadData called in the callback
- (void)setConfigSuccessCallback:(void (^_Nullable)(void))callback ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 '+ [ABUAdSDKManager addConfigLoadSuccessObserver:withAction:]' 方法代替");

/// 返回显示广告对应的Adn（该接口需要在bannerAdViewDidBecomVisible之后会返回对应的adn），当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3
- (ABUAdnType)getAdNetworkPlaformId;
/// 返回显示广告对应的rit（该接口需要在bannerAdViewDidBecomVisible之后会返回对应的rit），当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3
- (NSString *_Nullable)getAdNetworkRitId;
/// 返回显示广告对应的ecpm（该接口需要在bannerAdViewDidBecomVisible之后会返回对应的ecpm）），当未在平台配置ecpm会返回-1，当广告加载中未显示会返回-2，当没有权限访问该部分会放回-3 单位：分
- (NSString *_Nullable)getPreEcpm;

@end

NS_ASSUME_NONNULL_END
