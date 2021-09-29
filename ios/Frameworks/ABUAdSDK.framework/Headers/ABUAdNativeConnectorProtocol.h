//
//  ABUAdNativeConnectorProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/26.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUDislikeWords.h"
#import "ABUNativeAdView.h"
#import "ABUPlayerPlayState.h"

@protocol ABUAdNetworkAdapterProtocol;

@protocol ABUAdNativeConnectorProtocol <NSObject>
@optional
/// Tells the delegate that the adapter successfully set up a  ad.
- (void)adapterAdDidSetUp:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that the adapter failed to failed to set up a fullScreen video ad.
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter AdDidFailToSetUpWithError:(NSError *_Nullable)error;

/// Tells the delegate that the adapter successfully load Ads by nativeAdsManager
- (void)adapterAdManagerDidSuccessToLoad:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAds:(NSArray<NSObject *> *_Nullable)nativeAds;

/// Tells the delegate that the adapter failed load Ads by nativeAdsManager
- (void)adapterAdManagerDidFailToLoad:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter failError:(NSError *_Nullable)error;

/**
 This method is called when native ad slot has been shown.
 */
- (void)adapterAdDidVisible:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd;

/**
 This method is called when native ad is clicked.
 */
- (void)adapterAdDidClick:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd withView:(UIView *_Nullable)view;

/**
 * Sent after an ad view is clicked, a ad landscape view will present modal content
 */
- (void)adapterAdWillPresentFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd;

/**
 ** This method is called when FullScreen modal has been dismissed.
 *  全屏广告页将要关闭
 */
- (void)adapterAdWillDismissFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd;

///**
// This method is called when the Ad is closed.
// @param filterWords : reasons for dislike, the param is nil when there is no reasons
// */
//- (void)adapterAdDidClosed:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(NSObject *_Nullable)nativeAd dislikeReason:(NSArray<ABUDislikeWords *> *_Nullable)filterWords;

/**
 * Only for express Ad.only for expressAd,hasExpressAdGot = yes
 * This method is called when rendering a nativeExpressAdView successed, and nativeExpressAdView.size.height has been updated
 */
- (void)adapterAdExpressViewRenderSuccess:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd;

/**
 * Only for express Ad.only for expressAd,hasExpressAdGot = yes
 * This method is called when a nativeExpressAdView failed to render
 */
- (void)adapterAdExpressViewRenderFail:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(id _Nonnull)nativeAd error:(NSError *_Nullable)error;

/**
 This method is called when the Ad is closed.only for expressAd,hasExpressAdGot = yes
 */
- (void)adapterAdAdExpressViewDidClosed:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(NSObject *_Nullable)nativeAd closeReasons:(NSArray *_Nullable)filterWords;

# pragma mark - callback for native video Ads
/**
 This method is called when videoadview playback status changed.
 @param playerState : player state after changed
 */
- (void)adapterAdVideo:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(NSObject *_Nullable)nativeAd stateDidChanged:(ABUPlayerPlayState)playerState;

/**
 This method is called when videoadview's finish view is clicked.
 */
- (void)adapterAdVideoDidClick:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(NSObject *_Nullable)nativeAd;

/**
 This method is called when videoadview end of play.
 */
- (void)adapterAdVideoDidPlayFinish:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter nativeAd:(NSObject *_Nullable)nativeAd;

@end
