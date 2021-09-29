//
//  ABUAdapterRewardAdInfo.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/3/13.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ABUAdapterRewardAdInfo : NSObject

/**
 *  The reward name as defined on Self Service. Just for Admob & Mintergral & Sigmob.
 */
@property (nonatomic, copy) NSString *rewardName;

/**
 *  Amount of reward type given to the user. Just for Admob & Mintergral & Sigmob.
 */
@property (nonatomic, assign) NSInteger rewardAmount;

/**
 *  Id for trade from reward ad verify. Just for Gdt & Sigmob.
 */
@property (nonatomic, copy) NSString *tradeId;

@end

//typedef ABURewardInfo ABUAdapterRewardAdInfo;

NS_ASSUME_NONNULL_END
