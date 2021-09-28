//
//  ABUAdRequestConfig.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/24.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "ABUSize.h"

// 广告请求基础配置
@interface ABUAdRequestConfig : NSObject

@property (nonatomic, copy) NSString *appID;    // app id
@property (nonatomic, copy) NSString *slotID;   // 广告位id

@end

// Banner广告请求配置
@interface ABUAdBannerRequest : ABUAdRequestConfig

@property (nonatomic, strong) ABUSize *size;    // banner广告大小
@property (nonatomic, strong) UIViewController *rootViewController; // 根视图
@property (nonatomic, assign) NSInteger interval;   // 广告刷新间隔

@end

// Native广告请求配置
@interface ABUAdNativeRequest : ABUAdRequestConfig

@property (nonatomic, assign) NSInteger loadCount;    // 加载数量

@end
