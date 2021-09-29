//
//  ABUInterstitialAdDelegate.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUAdSDKDefines.h"
#import "ABUMaterialMeta.h"

@class ABUInterstitialAd;

@protocol ABUInterstitialAdDelegate <NSObject>
@optional

/**
 This method is called when interstitial ad material loaded successfully.
 */
- (void)interstitialAdDidLoad:(ABUInterstitialAd *_Nonnull)interstitialAd;

/**
 This method is called when interstitial ad material failed to load.
 @param error : the reason of error
 */
- (void)interstitialAd:(ABUInterstitialAd *_Nullable)interstitialAd didFailWithError:(NSError *_Nullable)error;

/**
 This method is called when a ExpressAdView failed to render.
 Only for expressAd,hasExpressAdGot = yes
 @param error : the reason of error
 */
- (void)interstitialAdViewRenderFail:(ABUInterstitialAd *_Nonnull)interstitialAd error:(NSError *__nullable)error;

/**
 This method is called when interstitial ad slot will be showing.
 */
- (void)interstitialAdDidVisible:(ABUInterstitialAd *_Nonnull)interstitialAd;

/**
 This method is called when video interstitialAd show API is called failed.
 */
- (void)interstitialAdDidShowFailed:(ABUInterstitialAd *_Nonnull)interstitialAd error:(NSError *_Nonnull)error;

/**
 This method is called when interstitial ad is clicked.
 */
- (void)interstitialAdDidClick:(ABUInterstitialAd *_Nonnull)interstitialAd;

/**
 This method is called when interstitial ad is closed.
 */
- (void)interstitialAdDidClose:(ABUInterstitialAd *_Nonnull)interstitialAd;

/**
 * This method is called when FullScreen modal has been presented.Include appstore jump.
 *  弹出详情广告页
 */
- (void)interstitialAdWillPresentFullScreenModal:(ABUInterstitialAd *_Nonnull)interstitialAd;

@end
