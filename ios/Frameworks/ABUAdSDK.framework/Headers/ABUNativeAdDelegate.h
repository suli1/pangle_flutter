//
//  ABUNativeAdDelegate.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/26.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUDislikeWords.h"
#import "ABUMaterialMeta.h"
#import "ABUPlayerPlayState.h"

@class ABUNativeAdsManager;
@class ABUNativeAdView;

# pragma mark - callback for native loader
@protocol ABUNativeAdsManagerDelegate <NSObject>
@optional

/// Native ad load success
/// @param adsManager adsManager
/// @param nativeAdViewArray Load successfully returned material
- (void)nativeAdsManagerSuccessToLoad:(ABUNativeAdsManager *_Nonnull)adsManager nativeAds:(NSArray<ABUNativeAdView *> *_Nullable)nativeAdViewArray;

/// Native ad load failed
/// @param adsManager adsManager
/// @param error  error description
- (void)nativeAdsManager:(ABUNativeAdsManager *_Nonnull)adsManager didFailWithError:(NSError *_Nullable)error;

@end

# pragma mark - callback for native video
@protocol ABUNativeAdViewDelegate <NSObject>
@optional

/**
 * This method is called when rendering a nativeExpressAdView successed, and nativeExpressAdView.size.height has been updated.
 * Only called when hasExpressAdGot = YES.
 */
- (void)nativeAdExpressViewRenderSuccess:(ABUNativeAdView *_Nonnull)nativeExpressAdView;

/**
 * This method is called when a nativeExpressAdView failed to render
 * Only called when hasExpressAdGot = YES.
 */
- (void)nativeAdExpressViewRenderFail:(ABUNativeAdView *_Nonnull)nativeExpressAdView error:(NSError *_Nullable)error;

/**
 This method is called when native ad slot has been shown.
 */
- (void)nativeAdDidBecomeVisible:(ABUNativeAdView *_Nonnull)nativeAdView;

/**
Sent when a player playback status changed.
@param playerState : player state after changed
 Only called when hasExpressAdGot = YES.
*/
- (void)nativeAdExpressView:(ABUNativeAdView *_Nonnull)nativeAdView stateDidChanged:(ABUPlayerPlayState)playerState;

/**
 This method is called when native ad is clicked.
 */
- (void)nativeAdDidClick:(ABUNativeAdView *_Nonnull)nativeAdView withView:(UIView *_Nullable)view;

/**
 * Sent after an ad view is clicked, a ad landscape view will present modal content.Include appstore jump.
 */
- (void)nativeAdViewWillPresentFullScreenModal:(ABUNativeAdView *_Nonnull)nativeAdView;

/**
 This method is called when the user clicked dislike reasons.
 Only called when nativeAdView.hasExpressAdGot = YES.
 */
- (void)nativeAdExpressViewDidClosed:(ABUNativeAdView *_Nullable)nativeAdView closeReason:(NSArray<ABUDislikeWords *> *_Nullable)filterWords;

@end

# pragma mark - callback for native video Ads
@protocol ABUNativeAdVideoDelegate <NSObject>
@optional

/**
 This method is called when videoadview playback status changed.
 @param playerState : player state after changed
 */
- (void)nativeAdVideo:(ABUNativeAdView *_Nullable)nativeAdView stateDidChanged:(ABUPlayerPlayState)playerState;

/**
 This method is called when videoadview's finish view is clicked.
 */
- (void)nativeAdVideoDidClick:(ABUNativeAdView *_Nullable)nativeAdView;

/**
 This method is called when videoadview end of play.
 */
- (void)nativeAdVideoDidPlayFinish:(ABUNativeAdView *_Nullable)nativeAdView;

@end
