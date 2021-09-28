//
//  ABUBaseNetworkConnectorProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/24.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ABUMaterialMeta.h"
#import "ABURewardedVideoAdDelegate.h"
#import "ABURewardInfo.h"
#import "ABUPlayerPlayState.h"

@protocol ABUAdNetworkAdapterProtocol;

@protocol ABUAdNetworkConnectorProtocol <NSObject>
@optional
# pragma mark - base callback for ad
/// Tells the delegate that the adapter successfully set up a  ad.
- (void)adapterAdDidSetUp:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that the adapter failed to failed to set up a fullScreen video ad.
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter AdDidFailToSetUpWithError:(NSError *_Nullable)error;

/// Tells the delegate that a  ad has loaded.
- (void)adapterAdDidLoad:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
  this methods is to tell delegate the type of native express rewarded video Ad
 */
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter videoAdType:(ABUVideoAdType)videoAdType;

/// Tells the delegate that a  ad failed to load.
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter didAdFailToLoadwithError:(NSError *_Nullable)error;

/**
 * Only for express Ad
 * This method is called when rendering a nativeExpressAd successed, and nativeExpressAdView.size.height has been updated
 */
- (void)adapterAdExpressRenderSuccess:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
 * Only for express Ad
 * This method is called when a nativeExpressAd failed to render
 */
- (void)adapterAdExpressRenderFail:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter error:(NSError *_Nullable)error;

/// Tells the delegate that a  ad has opened.
- (void)adapterAdWillVisible:
    (id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that a  ad has opened.
- (void)adapterAdDidVisible:
    (id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
 This method is called when video interstitialAd show API is called failed.
 */
- (void)adapterAdDidShowFailed:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter error:(NSError *_Nonnull)error;

/// Tells the delegate that a  ad has closed.
- (void)adapterAdDidClose:
    (id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
 * This method is called when FullScreen modal has been presented.
 *  弹出全屏广告页
 */
- (void)adapterAdWillPresentFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
 ** This method is called when FullScreen modal has been dismissed.
 *  全屏广告页将要关闭
 */
- (void)adapterAdWillDismissFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/**
Sent when a playerw playback status changed.
 @param adAdapter : player state after changed
 @param playerState : player state after changed
*/
- (void)adapterAd:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter stateDidChanged:(ABUPlayerPlayState)playerState;

/// Tells the delegate that a  ad was clicked.
- (void)adapterAdDidClick:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that a  ad was clicked skip.
- (void)adapterAdDidClickSkip:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

# pragma mark - callback video ad
/// eg:RewardVedioAd, fullscreenVedioAd

/// Tells the delegate that a video has been downloaded.
- (void)adapterAdDidDownloadVideo:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that a video ad has completed playing.
- (void)adapterDidPlayFinishVideoAd:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter didFailWithError:(NSError *_Nullable)error;

/// Tells the delegate that server verification is succeeded.
- (void)adapterDidServerRewardedVideoAdSucceed:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter rewardAdInfo:(ABUAdapterRewardAdInfo *_Nullable)rewardAdInfo verify:(BOOL)verify;

# pragma mark - callback for other viewcontroller

/**
 * Sent when a player finished
 * @param error : error of player
 */
- (void)adapterAdExpressPlayerDidPlayFinish:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter error:(NSError *_Nullable)error;

# pragma mark - For splash ad
- (void)adapterAdSplashLifeTime:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter lifeTime:(NSInteger)time;

# pragma mark - For zoomout splash ad

/**
 This method is called when splash ad is clicked.
 */
- (void)adapterAdSplashZoomOutDidClick:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when splash ad is closed.
 */
- (void)adapterAdSplashZoomOutDidClose:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when another controller will open.
 */
- (void)adapterAdSplashZoomOutWillPresentFullScreenModal:(UIView *_Nonnull)splashZoomOutView;

/**
 This method is called when another controller has been closed.
 */
- (void)adapterAdSplashZoomOutDidDismissFullScreenModal:(UIView *_Nonnull)splashZoomOutView;


@end
