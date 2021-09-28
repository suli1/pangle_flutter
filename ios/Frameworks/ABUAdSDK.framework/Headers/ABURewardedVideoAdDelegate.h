//
//  ABURewardedVideoAdDelegate.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/25.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import "ABUMaterialMeta.h"

/// define the type of rewarded video ad
typedef NS_ENUM (NSUInteger, ABUVideoAdType) {
    ABUVideoAdTypeEndcard       = 0,     // video + endcard
    ABUVideoAdTypeVideoPlayable = 1,     // video + playable
    ABUVideoAdTypePurePlayable  = 2      // pure playable
};

@class ABURewardedVideoAd;
@class ABUAdapterRewardAdInfo;

@protocol ABURewardedVideoAdDelegate <NSObject>

@optional

/**
 This method is called when video ad material loaded successfully.
 */
- (void)rewardedVideoAdDidLoad:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 This method is called when video ad materia failed to load.
 @param error : the reason of error
 */
- (void)rewardedVideoAd:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd didFailWithError:(NSError *_Nullable)error;

/**
 This method is called when cached successfully.
 */
- (void)rewardedVideoAdDidDownLoadVideo:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 This method is called when video ad slot has been shown.
 */
- (void)rewardedVideoAdDidVisible:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 This method is called when video ad show API is called failed.
 */
- (void)rewardedVideoAdDidShowFailed:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd error:(NSError *_Nonnull)error;

/**
 This method is called when video ad is clicked.
 */
- (void)rewardedVideoAdDidClick:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 This method is called when video ad is skiped.
 */
- (void)rewardedVideoAdDidSkip:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 This method is called when video ad is closed.
 */
- (void)rewardedVideoAdDidClose:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd;

/**
 Server verification which is requested asynchronously is succeeded.include C2C and S2S methods .
 @param verify :return YES when Meet the reward conditions.
 */
- (void)rewardedVideoAdServerRewardDidSucceed:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd verify:(BOOL)verify ABU_DEPRECATED_MSG_ATTRIBUTE("接口即将废弃，请使用 rewardedVideoAdServerRewardDidSucceed:rewardInfo:verify:");

/**
 Server verification which is requested asynchronously is succeeded.include C2C and S2S methods. And S2S is only work for BUAdSDK & GDT(v4.12.3+).
 @param rewardInfo :return rewardInfo when adn are Admob or Mintergral or GDT.
 @param verify :return YES when Meet the reward conditions.
 */
- (void)rewardedVideoAdServerRewardDidSucceed:(ABURewardedVideoAd *_Nonnull)rewardedVideoAd rewardInfo:(ABUAdapterRewardAdInfo *_Nullable)rewardInfo verify:(BOOL)verify;

/**
 This method is called when video ad play completed or an error occurred.All adns except Admob support.
 @param error : the reason of error
 */
- (void)rewardedVideoAdDidPlayFinish:(ABURewardedVideoAd * _Nonnull)rewardedVideoAd didFailWithError:(NSError *_Nullable)error;



@end
