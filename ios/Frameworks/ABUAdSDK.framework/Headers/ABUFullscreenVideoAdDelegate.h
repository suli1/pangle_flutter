//
//  Header.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUPlayerPlayState.h"

typedef NS_ENUM (NSUInteger, ABUFullScreenVideoAdType) {
    ABUFullScreenAdTypeEndcard       = 0,     // video + endcard
    ABUFullScreenAdTypeVideoPlayable = 1,     // video + playable
    ABUFullScreenAdTypePurePlayable  = 2      // pure playable
};

@class ABUFullscreenVideoAd;

@protocol ABUFullscreenVideoAdDelegate <NSObject>

@optional

/**
 This method is called when video ad material loaded successfully.
 */
- (void)fullscreenVideoAdDidLoad:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 This method is called when video ad materia failed to load.
 @param error : the reason of error
 */
- (void)fullscreenVideoAd:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error;

/**
 This method is called when cached successfully.
 */
- (void)fullscreenVideoAdDidDownLoadVideo:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 This method is called when video ad slot will be showing.
 */
- (void)fullscreenVideoAdDidVisible:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 This method is called when video ad show API is called failed.
 */
- (void)fullscreenVideoAdDidShowFailed:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd error:(NSError *_Nonnull)error;

/**
 This method is called when video ad is clicked.
 */
- (void)fullscreenVideoAdDidClick:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 This method is called when video ad is skiped.
 */
- (void)fullscreenVideoAdDidSkip:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 This method is called when video ad is closed.
 */
- (void)fullscreenVideoAdDidClose:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;

/**
 * This method is called when FullScreen modal has been presented.Include appstore jump.
 *  弹出详情广告页
 */
- (void)fullscreenVideoAdWillPresentFullScreenModal:(ABUFullscreenVideoAd *_Nonnull)fullscreenVideoAd;


/**
 This method is called when video ad play completed or an error occurred.All adns support except Admob & Baidu.
 @param error : the reason of error
 */
- (void)fullscreenVideoAdDidPlayFinish:(ABUFullscreenVideoAd * _Nonnull)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error;

@end
