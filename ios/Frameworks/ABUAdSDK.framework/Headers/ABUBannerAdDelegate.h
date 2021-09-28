//
//  ABUBannerAdDelegate.h
//  ABUAdSDK
//
//  Created by XuQingJia on 2020/11/22.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class ABUBannerAd;

@protocol ABUBannerAdDelegate <NSObject>

/**
 This method is called when bannerAdView ad slot loaded successfully.
 @param bannerView : view for bannerView
 */
- (void)bannerAdDidLoad:(ABUBannerAd *_Nonnull)bannerAd bannerView:(UIView *)bannerView;

/**
 This method is called when bannerAdView ad slot failed to load.
 @param error : the reason of error
 */
- (void)bannerAd:(ABUBannerAd *_Nonnull)bannerAd didLoadFailWithError:(NSError *_Nullable)error;

/**
This method is called when bannerAdView ad slot success to show.
*/
- (void)bannerAdDidBecomVisible:(ABUBannerAd *_Nonnull)ABUBannerAd bannerView:(UIView *)bannerView;

/**
 * This method is called when FullScreen modal has been presented.Include appstore jump.
 *  弹出详情广告页
 */
- (void)bannerAdWillPresentFullScreenModal:(ABUBannerAd *_Nonnull)ABUBannerAd bannerView:(UIView *)bannerView;

/**
 ** This method is called when FullScreen modal has been dismissed.Include appstore jump.
 *  详情广告页将要关闭
 */
- (void)bannerAdWillDismissFullScreenModal:(ABUBannerAd *_Nonnull)ABUBannerAd bannerView:(UIView *)bannerView;

/**
 This method is called when bannerAdView is clicked.
 */
- (void)bannerAdDidClick:(ABUBannerAd *_Nonnull)ABUBannerAd bannerView:(UIView *)bannerView;

/**
 This method is called when the user clicked dislike button and chose dislike reasons.
 @param filterwords : the array of reasons for dislike.
*/

- (void)bannerAdDidClosed:(ABUBannerAd *_Nonnull)ABUBannerAd bannerView:(UIView *)bannerView dislikeWithReason:(NSArray<ABUDislikeWords *> *_Nullable)filterwords;
@end

NS_ASSUME_NONNULL_END
