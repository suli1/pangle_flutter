//
//  ABUAdViewWitnessChecker.h
//  ABUAdSDK
//
//  Created by wangchaop on 22/06/2020.
//  Copyright © 2017 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUViewTracker.h"

@interface ABUAdViewWitnessChecker : NSObject

+ (instancetype)sharedInstance;

+ (NSTimeInterval)getWitnessTimeForAd:(id<ABUAd>)materialMeta;

+ (void)setWitnessTimeForAd:(id<ABUAd>)materialMeta;

- (void)removeAd:(id<ABUAd>)ad;

- (void)addViewTracker:(id<ABUViewTracker>)tracker;

- (void)removeAllTracker;

@end
