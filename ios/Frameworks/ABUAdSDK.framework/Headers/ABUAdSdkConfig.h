//
//  ABUAdSdkConfig.h
//  ABUAdSDK
//
//  Created by CHAORS on 2021/4/25.
//

#import <Foundation/Foundation.h>
#import "ABUAdSDKDefines.h"

NS_ASSUME_NONNULL_BEGIN

@interface ABUAdSdkConfig : NSObject

@property (nonatomic, copy) NSDictionary *publisherDidMap; // 用于第三方的did传递给穿山甲(目前用于Ohayoo)
@property (nonatomic, assign) ABUAdSDKThemeStatus themeStatus;  // 主题模式

@end

NS_ASSUME_NONNULL_END
