//
//  ABUSplashAdDelegate.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUMaterialMeta.h"

@class ABUSplashAd;

@protocol ABUSplashAdDelegate <NSObject>

@optional
/**
 This method is called when splash ad material loaded successfully.
 */
- (void)splashAdDidLoad:(ABUSplashAd *_Nonnull)splashAd;

/**
 This method is called when splash ad material failed to load.
 @param error : the reason of error
 */
- (void)splashAd:(ABUSplashAd *_Nonnull)splashAd didFailWithError:(NSError *_Nullable)error;

/**
 This method is called when splash ad slot will be showing.
 */
- (void)splashAdWillVisible:(ABUSplashAd *_Nonnull)splashAd;

/**
 This method is called when splash ad slot will be showing.
 */
- (void)splashAdDidShowFailed:(ABUSplashAd *_Nonnull)splashAd error:(NSError *)error;

/**
 This method is called when splash ad is clicked.
 */
- (void)splashAdDidClick:(ABUSplashAd *_Nonnull)splashAd;

/**
 This method is called when splash ad is closed.
 */
- (void)splashAdDidClose:(ABUSplashAd *_Nonnull)splashAd;

/**
 * This method is called when FullScreen modal has been presented.Include appstore jump.
 *  弹出全屏广告页或者appstore
 */
- (void)splashAdWillPresentFullScreenModal:(ABUSplashAd *_Nonnull)splashAd;

/**
 * This method is called when FullScreen modal has been dissmissed.Include appstore jump.
 *  关闭全屏广告页或者appstore.MTG无该回调
 */
- (void)splashAdWillDissmissFullScreenModal:(ABUSplashAd *_Nonnull)splashAd;

/**
 This method is called when spalashAd countdown equals to zero.Work for adn network except Baidu Ad SDK.
 */
- (void)splashAdCountdownToZero:(ABUSplashAd *_Nonnull)splashAd;

/**
 * Sent when a player finished. for express Ads when splashAd.hasExpressAdGot = YES
 * @param error : error of player.Only for BuAdSDK.
 */
- (void)splashAdExpressViewDidPlayFinish:(ABUSplashAd *_Nonnull)splashAd error:(NSError *_Nullable)error;

@end
