//
//  ABUAdBannerConnectorProtocol.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUDislikeWords.h"

@protocol ABUAdNetworkAdapterProtocol;

@protocol ABUAdBannerConnectorProtocol <NSObject>
@optional
/// Tells the delegate that the adapter successfully set up a  ad.
- (void)adapterAdDidSetUp:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter;

/// Tells the delegate that the adapter failed to failed to set up a fullScreen video ad.
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter AdDidFailToSetUpWithError:(NSError *_Nullable)error;

/// Tells the delegate that a  ad has loaded.
- (void)adapterAdDidLoad:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/// Tells the delegate that a  ad failed to load.
- (void)adapter:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter didAdFailToLoadwithError:(NSError *_Nullable)error;

/// Tells the delegate that a  ad will be showing.
- (void)                               adapterAdWillVisible:
    (id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/// Tells the delegate that a  ad has opened.
- (void)                                adapterAdDidVisible:
    (id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/**
 * This method is called when FullScreen modal has been presented.
 *  弹出全屏广告页
 */
- (void)adapterAdWillPresentFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/**
 ** This method is called when FullScreen modal has been dismissed.
 *  全屏广告页将要关闭
 */
- (void)adapterAdWillDismissFullScreenModal:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/// Tells the delegate that a  ad was clicked.
- (void)adapterAdDidClick:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/// Tells the delegate that a  ad was render success.Only for template Ads
- (void)adapterAdDidViewRenderSuccess:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView;

/// Tells the delegate that a  ad was render fail.Only for template Ads
- (void)adapterAdDidViewRenderFail:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView error:(NSError *__nullable)error;

- (void)adapterAdDidClosed:(id<ABUAdNetworkAdapterProtocol>_Nonnull)adAdapter view:(UIView *_Nonnull)adView dislikeReason:(NSArray<ABUDislikeWords *> *_Nullable)filterwords;

@end
