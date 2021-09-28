//
//  ABUNativeAdsManager.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/26.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ABUBaseAggregationAd.h"
#import "ABUAdUnit.h"
#import "ABUNativeAdDelegate.h"
#import "ABUAdSDKDefines.h"

///ABUNativeAdsManager
@interface ABUNativeAdsManager : ABUBaseAggregationAd

/// The delegate for receiving state change messages such as requests succeeding/failing.  The delegate can be set to any object which conforming to <ABUNativeAdsManagerDelegate>.
@property (nonatomic, weak, nullable) id<ABUNativeAdsManagerDelegate> delegate;

/// Need to load ads
@property (nonatomic, assign, readonly) NSInteger loadCount;

/**
 该字段用于标示配置是否请求成功，具体使用参考demo
 */
@property (nonatomic, assign, readonly) BOOL hasAdConfig ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 '+ [ABUAdSDKManager configDidLoad]' 方法代替");

/**
 required.
 Root view controller for handling ad actions.
 Action method includes 'pushViewController' and 'presentViewController'.
 */
@property (nonatomic, weak, readwrite) UIViewController *_Nullable rootViewController;

/// Indicates whether videos should start muted. By default this property value is YES. Some ads take priority from server configuration.Now supported adns are:Admob, Baidu, Gdt, and MTG.
@property (nonatomic, assign) BOOL startMutedIfCan;

/// size expected ad view size，when size.height is zero, acture height will match size.width rsetep when getExpressAdIfCan is YES
@property (nonatomic, assign, readwrite) CGSize adSize;

/// use express 2.0 first if it is supported, only for gdt, required gdt version ≥ 4.11.9.And v2700+ the property will be prioritized based on the platform configuration. Valid Only getExpressAdIfCan = YES.
@property (nonatomic, assign) BOOL useExpress2IfCanForGDT ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，在SDK V2900以上结合GDT V4.12.80以上 Gdt的原生模板广告在客户端无需区分2.0和1.0");

/// Initialization method
/// @param adSlot Data for loading ads
- (instancetype _Nonnull)initWithSlot:(ABUAdUnit *_Nullable)adSlot;

/// Load ads
/// @param count Number of ads load.The maximum value is 3 if adSlot.getExpressAdIfCan is YES, Otherwise the maximum value is 5.But the number of ads filled by different adn may be different.
- (void)loadAdDataWithCount:(NSInteger)count;

/**
 在hasAdConfig为NO的情况下需要传入该callback，并在callback中loadAdData，具体使用参考demo
 */
- (void)setConfigSuccessCallback:(void (^_Nullable)(void))callback ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 '+ [ABUAdSDKManager addConfigLoadSuccessObserver:withAction:]' 方法代替");

//required.
//清除广告资源，在广告使用完毕（当前页面销毁）手动调用
- (void)destroyAd;


@end
