//
//  ABUSdkInitConfig.h
//  ABUAdSDK
//
//  Created by wangchao on 2020/2/24.
//  Copyright © 2020 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ABUPrivacyConfig.h"

NS_ASSUME_NONNULL_BEGIN

@interface ABUSdkInitConfig : NSObject

@property (nonatomic, copy) NSString *appID;
@property (nonatomic, copy) NSString *appKey;

@property (nonatomic, assign) BOOL testUnityAd; // 设置unityAdSDK测试模式；无配置默认为NO

// 隐私合规配置
@property (nonatomic, copy) NSDictionary *privacyConfig;

@end

NS_ASSUME_NONNULL_END
